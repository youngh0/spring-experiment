package com.example.experiment.optimisticlock;

import com.example.experiment.optimisticlock.repo.CouponRepository;
import com.example.experiment.optimisticlock.repo.StampRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final StampRepository stampRepository;

    @Transactional
    public void insert() {
        Coupon coupon = couponRepository.findById(1L).get();
        Stamp stamp = stampRepository.save(new Stamp(coupon.getId()));

        coupon.updateName("ee");
    }
}
