package com.example.experiment.optimisticlock.repo;

import com.example.experiment.optimisticlock.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
