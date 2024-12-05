package com.example.elevendash.domain.coupon.dto.response;

import com.example.elevendash.domain.coupon.dto.CouponSummary;

import java.util.List;

public record CouponListResponse(
        List<CouponSummary> couponSummaries
) {
}
