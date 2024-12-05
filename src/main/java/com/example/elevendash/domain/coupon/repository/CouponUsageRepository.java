package com.example.elevendash.domain.coupon.repository;

import com.example.elevendash.domain.coupon.dto.CouponSummary;
import com.example.elevendash.domain.coupon.entity.CouponUsage;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    // Count the number of usages for a coupon within a date range
    long countByCouponIdAndCreatedAtBetween(Long couponId, LocalDateTime startDate, LocalDateTime endDate);

    // Check if a member has already used a specific coupon
    boolean existsByMemberIdAndCouponId(Long memberId, Long couponId);

    // Retrieve usage details for a member and coupon
    Optional<CouponUsage> findFirstByMemberIdAndCouponId(Long memberId, Long couponId);

    default CouponUsage findFirstByMemberIdAndCouponIdOrThrow(Long memberId, Long couponId) {
        return findFirstByMemberIdAndCouponId(memberId, couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COUPON));
    }

    @Query("""
            SELECT new com.example.elevendash.domain.coupon.dto.CouponSummary(cu.id, cu.coupon.name, cu.coupon.type, cu.coupon.discountValue, cu.createdAt, cu.coupon.expirationDate) FROM CouponUsage cu
            WHERE cu.member.id = :memberId AND cu.coupon.expirationDate > :now AND cu.used IS NOT TRUE
            """)
    List<CouponSummary> findActiveCoupons(Long memberId, LocalDateTime now);


}
