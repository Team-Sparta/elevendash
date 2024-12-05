package com.example.elevendash.domain.coupon.controller;

import com.example.elevendash.domain.coupon.dto.response.CouponIdResponse;
import com.example.elevendash.domain.coupon.service.CouponService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<CommonResponse<CouponIdResponse>> issueCoupon(
            @RequestParam Long couponId,
            @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS, couponService.issueCoupon(loginMember, couponId));
    }
}
