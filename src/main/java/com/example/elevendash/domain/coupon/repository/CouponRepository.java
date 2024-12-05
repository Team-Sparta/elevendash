package com.example.elevendash.domain.coupon.repository;

import com.example.elevendash.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByExpirationDateBefore(LocalDateTime now);
}
