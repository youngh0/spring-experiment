package com.example.experiment.mysqlnamedlock.service;

import com.example.experiment.mysqlnamedlock.domain.Lecture;
import com.example.experiment.mysqlnamedlock.domain.LectureStudent;
import com.example.experiment.mysqlnamedlock.repository.LectureRepository;
import com.example.experiment.mysqlnamedlock.repository.LectureStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureStudentRepository lectureStudentRepository;

    @Transactional
    public Long create(int number) {
        Lecture lecture = new Lecture(number);
        return lectureRepository.save(lecture).getId();
    }

    @Transactional
    public void enrolment(Long lectureId, Long studentId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow();

        if (lecture.isPossibleEnrolment()) {
            lectureStudentRepository.save(new LectureStudent(lecture, studentId));
            lecture.decrease();
        }
    }

    @Transactional(readOnly = true)
    public int countLectureStudents(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        return lectureStudentRepository.countByLecture(lecture);
    }
}
