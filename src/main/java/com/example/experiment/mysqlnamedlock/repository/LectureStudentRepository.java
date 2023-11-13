package com.example.experiment.mysqlnamedlock.repository;

import com.example.experiment.mysqlnamedlock.domain.Lecture;
import com.example.experiment.mysqlnamedlock.domain.LectureStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureStudentRepository extends JpaRepository<LectureStudent, Long> {

    int countByLecture(Lecture lecture);
}
