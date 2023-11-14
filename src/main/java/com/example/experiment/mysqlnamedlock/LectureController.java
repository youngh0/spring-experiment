package com.example.experiment.mysqlnamedlock;

import com.example.experiment.mysqlnamedlock.facade.FacadeLectureService;
import com.example.experiment.mysqlnamedlock.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;
    private final FacadeLectureService facadeLectureService;

    @PostMapping("/lecture/{lectureId}")
    public ResponseEntity<Void> enrolment(@PathVariable Long lectureId, @RequestBody LectureRequest lectureRequest) {
        lectureService.enrolment(lectureId, lectureRequest.getStudentId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/lecture/namedLock/{lectureId}")
    public ResponseEntity<Void> enrolmentWithNamedLock(@PathVariable Long lectureId, @RequestBody LectureRequest lectureRequest) {
        facadeLectureService.enrolment(lectureId, lectureRequest.getStudentId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/lecture")
    public ResponseEntity<Long> create(@RequestBody LectureCreateRequest lectureCreateRequest) {
        Long savedId = lectureService.create(lectureCreateRequest.getNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedId);
    }

    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<Integer> countStudents(@PathVariable Long lectureId) {
        int count = lectureService.countLectureStudents(lectureId);

        return ResponseEntity.status(HttpStatus.CREATED).body(count);
    }
}

class LectureCreateRequest {
    private Integer number;

    public LectureCreateRequest() {
    }

    public LectureCreateRequest(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }
}
