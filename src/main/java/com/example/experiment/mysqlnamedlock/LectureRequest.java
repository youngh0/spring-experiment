package com.example.experiment.mysqlnamedlock;

public class LectureRequest {

    private Long studentId;

    public LectureRequest() {
    }

    public LectureRequest(Long studentId) {
        this.studentId = studentId;
    }

    public Long getStudentId() {
        return studentId;
    }
}
