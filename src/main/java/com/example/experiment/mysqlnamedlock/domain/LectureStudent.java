package com.example.experiment.mysqlnamedlock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class LectureStudent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Lecture lecture;
    private Long studentId;

    public LectureStudent() {
    }

    public LectureStudent(Lecture lecture, Long studentId) {
        this.lecture = lecture;
        this.studentId = studentId;
    }
}
