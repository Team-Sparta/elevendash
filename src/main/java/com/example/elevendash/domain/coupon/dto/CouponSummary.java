package com.example.elevendash.domain.coupon.dto;

import com.example.elevendash.domain.coupon.enums.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponSummary(
        Long issuedCouponId,
        String name,
        CouponType couponType,
        BigDecimal discountPrice,
        LocalDateTime issuedAt,
        LocalDateTime expirationDate
) {
}
