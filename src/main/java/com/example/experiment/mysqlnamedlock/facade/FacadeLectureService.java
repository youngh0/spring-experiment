package com.example.experiment.mysqlnamedlock.facade;

import com.example.experiment.mysqlnamedlock.repository.LockRepository;
import com.example.experiment.mysqlnamedlock.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FacadeLectureService {

    private final LectureService lectureService;
    private final LockRepository lockRepository;

    public void enrolment(Long lectureId, Long studentId) {
        lockRepository.executeWithLock("lecture_enrolment", 3000,
                () -> lectureService.enrolmentWithNamedLock(lectureId, studentId));
    }
}
