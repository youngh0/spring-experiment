package com.example.experiment.mysqlnamedlock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer restCount; // 남은 개수

    public Lecture() {
    }

    public Lecture(Integer restCount) {
        this.restCount = restCount;
    }

    public Lecture(Long id, Integer restCount) {
        this.id = id;
        this.restCount = restCount;
    }

    public boolean isPossibleEnrolment() {
        return restCount > 0;
    }

    public void decrease() {
        restCount--;
    }

    public Long getId() {
        return id;
    }
}
