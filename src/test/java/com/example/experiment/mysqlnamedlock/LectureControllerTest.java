package com.example.experiment.mysqlnamedlock;

import com.example.experiment.DataCleaner;
import com.example.experiment.DataClearExtension;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(DataClearExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class LectureControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DataCleaner cleaner;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        cleaner.clear();
    }

    @Test
    void 싱글_스레드_수강_신청() {
        int maxLectureStudentNum = 10;

        Long lectureId = 강의등록(maxLectureStudentNum);
        for (int i = 0; i < 50; i++) {
            수강신청(lectureId);
        }
        int lectureStudentNum = 강의_수강생_조회(lectureId);
        assertThat(lectureStudentNum).isEqualTo(maxLectureStudentNum);
    }

    @Test
    void 다중_요청_수강_신청() throws InterruptedException {
        int maxLectureStudentNum = 10;
        Long lectureId = 강의등록(maxLectureStudentNum);

        int studentsNum = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        CountDownLatch countDownLatch = new CountDownLatch(studentsNum);

        for (int i = 0; i < studentsNum; i++) {
            executorService.submit(() -> {
                try {
//                    수강신청(lectureId);
                    락을_이용한_수강신청(lectureId);
//                    락과_AOP를_이용한_수강신청(lectureId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        int lectureStudentNum = 강의_수강생_조회(lectureId);
        assertThat(lectureStudentNum).isEqualTo(maxLectureStudentNum);
    }

    private Long 강의등록(int number) {
        ExtractableResponse<Response> extract = RestAssured.given()
                .contentType(JSON)
                .body(new LectureCreateRequest(number))
                .when()
                .post("/lecture")
                .then()
                .extract();
        ResponseBody body = extract.response().body();
        return body.as(Long.class);
    }

    private void 수강신청(Long lectureId) {
        RestAssured.given()
                .contentType(JSON)
                .body(new LectureRequest(3L))
                .when()
                .post("/lecture/{lectureId}", lectureId)
                .then()
                .extract();
    }

    private void 락을_이용한_수강신청(Long lectureId) {
        RestAssured.given()
                .contentType(JSON)
                .body(new LectureRequest(3L))
                .when()
                .post("/lecture/namedLock/{lectureId}", lectureId)
                .then()
                .extract();
    }

    private void 락과_AOP를_이용한_수강신청(Long lectureId) {
        RestAssured.given()
                .contentType(JSON)
                .body(new LectureRequest(3L))
                .when()
                .post("/lecture/namedLock/aop/{lectureId}", lectureId)
                .then()
                .extract();
    }

    private int 강의_수강생_조회(Long lectureId) {
        ExtractableResponse<Response> extract = RestAssured.given()
                .contentType(JSON)
                .when()
                .get("/lecture/{lectureId}", lectureId)
                .then()
                .extract();

        return extract.response().body().as(Integer.class);
    }
}
