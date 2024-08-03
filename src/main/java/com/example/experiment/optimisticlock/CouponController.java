package com.example.experiment.optimisticlock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/stamp")
    public ResponseEntity<Void> insert() {

        couponService.insert();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
