package com.example.experiment.mysqlnamedlock.service;

import com.example.experiment.mysqlnamedlock.domain.Lecture;
import com.example.experiment.mysqlnamedlock.domain.LectureStudent;
import com.example.experiment.mysqlnamedlock.repository.LectureRepository;
import com.example.experiment.mysqlnamedlock.repository.LectureStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;

@RequiredArgsConstructor
@Slf4j
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureStudentRepository lectureStudentRepository;
    private final DataSource dataSource;

    @Transactional
    public Long create(int number) {
        Lecture lecture = new Lecture(number);
        return lectureRepository.save(lecture).getId();
    }

    @Transactional
    public void enrolment(Long lectureId, Long studentId) {
        ConnectionHolder connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        Connection connection = connectionHolder.getConnection();
        log.info("business Thread: {}, conn: {}", Thread.currentThread().getName(), connection);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow();

        if (lecture.isPossibleEnrolment()) {
            lectureStudentRepository.save(new LectureStudent(lectureId, studentId));
            lecture.decrease();
            return;
        }
        log.info("{} 수강신청 실패", Thread.currentThread().getName());
    }

    @Transactional
    public void enrolmentWithNamedLock(Long lectureId, Long studentId) {
        ConnectionHolder connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        Connection connection = connectionHolder.getConnection();
        log.info("business Thread: {}, conn: {}", Thread.currentThread().getName(), connection);
        log.info("main datasource: {}", dataSource.toString());
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow();

        if (lecture.isPossibleEnrolment()) {
            lectureStudentRepository.save(new LectureStudent(lectureId, studentId));
            lecture.decrease();
            return;
        }

        log.info("{} 수강신청 실패", Thread.currentThread().getName());
    }

    @Transactional(readOnly = true)
    public int countLectureStudents(Long lectureId) {
        lectureRepository.findById(lectureId).orElseThrow();
        return lectureStudentRepository.countByLectureId(lectureId);
    }
}
