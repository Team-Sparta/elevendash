package com.example.elevendash.domain.coupon.service;

import com.example.elevendash.domain.coupon.dto.response.CouponIdResponse;
import com.example.elevendash.domain.coupon.dto.response.CouponListResponse;
import com.example.elevendash.domain.coupon.entity.Coupon;
import com.example.elevendash.domain.coupon.entity.CouponUsage;
import com.example.elevendash.domain.coupon.enums.CouponType;
import com.example.elevendash.domain.coupon.repository.CouponRepository;
import com.example.elevendash.domain.coupon.repository.CouponUsageRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private final CouponUsageRepository couponUsageRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CouponIdResponse issueCoupon(Long memberId, Long couponId) {
        LocalDateTime now = LocalDateTime.now();

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COUPON));

        boolean isAlreadyIssued = couponUsageRepository.existsByMemberIdAndCouponId(memberId, couponId);

        // 오늘 발급된 쿠폰 수가 하루 발급 개수 제한을 초과했는지 확인
        long todayIssuedCount = couponUsageRepository.countByCouponIdAndCreatedAtBetween(
                couponId,
                now.toLocalDate().atStartOfDay(),
                now.toLocalDate().plusDays(1).atStartOfDay());

        coupon.validateCoupon(now, isAlreadyIssued, todayIssuedCount);

        // 쿠폰 발급
        CouponUsage couponUsage = new CouponUsage(coupon, memberId, false);
        couponUsageRepository.save(couponUsage);

        // 쿠폰 발급 개수 업데이트
        coupon.updateIssuedCount();
        Coupon issuedCoupon = couponRepository.save(coupon);
        return new CouponIdResponse(issuedCoupon.getId());
    }

    public CouponListResponse getMyCoupons(Long memberId) {
        return new CouponListResponse(couponUsageRepository.findActiveCoupons(memberId, LocalDateTime.now()));
    }

    @Transactional
    public void useCoupon(Long memberId, Long couponId) {
        CouponUsage issuedCoupon = couponUsageRepository.findFirstByMemberIdAndCouponIdOrThrow(memberId, couponId);

        issuedCoupon.validate();

        issuedCoupon.useCoupon();
    }

    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal originalAmount) {
        BigDecimal discount = BigDecimal.ZERO;

        if (coupon.getType() == CouponType.PERCENTAGE) {
            discount = originalAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            if (coupon.getMaxDiscountAmount() != null && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount(); // 최대 할인 금액 초과 시 최대값으로 제한
            }
        } else if (coupon.getType() == CouponType.FIXED_AMOUNT) {
            discount = coupon.getDiscountValue();
        }

        return discount;
    }
}
