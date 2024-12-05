package com.example.elevendash.domain.coupon.repository;

import com.example.elevendash.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
