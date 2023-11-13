package com.example.experiment.mysqlnamedlock.repository;

import com.example.experiment.mysqlnamedlock.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
