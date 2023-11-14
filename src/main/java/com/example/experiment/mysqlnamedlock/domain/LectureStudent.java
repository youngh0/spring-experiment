package com.example.experiment.mysqlnamedlock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class LectureStudent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long lectureId;
    private Long studentId;

    public LectureStudent() {
    }

    public LectureStudent(Long lectureId, Long studentId) {
        this.lectureId = lectureId;
        this.studentId = studentId;
    }
}
