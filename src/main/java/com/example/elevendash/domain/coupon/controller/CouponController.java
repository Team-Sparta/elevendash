package com.example.elevendash.domain.coupon.controller;

import com.example.elevendash.domain.coupon.dto.response.CouponIdResponse;
import com.example.elevendash.domain.coupon.service.CouponService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "쿠폰 API",
        description = "쿠폰 발급 및 관리와 관련된 API"
)
@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(
            summary = "쿠폰 발급",
            description = "사용자가 특정 쿠폰 ID로 쿠폰을 발급받습니다."
    )
    @PostMapping("/issue")
    public ResponseEntity<CommonResponse<CouponIdResponse>> issueCoupon(
            @Parameter(description = "발급받을 쿠폰의 ID", example = "123")
            @RequestParam Long couponId,

            @Parameter(hidden = true)
            @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS, couponService.issueCoupon(loginMember, couponId));
    }
}