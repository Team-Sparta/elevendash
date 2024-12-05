package com.example.elevendash.domain.coupon.entity;

import com.example.elevendash.domain.coupon.enums.CouponType;
import com.example.elevendash.global.entity.BaseCreatedTimeEntity;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "coupons"
)
public class Coupon extends BaseCreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '쿠폰 고유 번호'")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255) comment '쿠폰 이름'")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20) comment '쿠폰 타입'")
    private CouponType type;

    private BigDecimal discountValue; // 할인 금액 또는 할인 비율 (정액일 경우 금액, 정률일 경우 비율)

    private BigDecimal maxDiscountAmount; // 정률 쿠폰일 경우 최대 할인 금액 (없으면 null)

    private LocalDateTime expirationDate; // 만료 일자

    private Integer totalIssuedCount; // 총 발급 가능 개수

    private Integer dailyIssuedCountLimit; // 하루 발급 한도

    private Integer issuedCount; // 현재 발급된 쿠폰 수

    public void validateCoupon(LocalDateTime now, boolean isAlreadyIssued, Long todayIssuedCount) {

        if (isAlreadyIssued) {
            throw new BaseException(ErrorCode.ALREADY_ISSUED_COUPON);
        }

        if (this.getExpirationDate().isBefore(now)) {
            throw new BaseException(ErrorCode.EXPIRED_COUPON);
        }

        if (this.getIssuedCount() >= this.getTotalIssuedCount()) {
            throw new BaseException(ErrorCode.TOTAL_OVER_ISSUED_COUPON);
        }

        if (todayIssuedCount >= this.getDailyIssuedCountLimit()) {
            throw new BaseException(ErrorCode.TOTAL_OVER_ISSUED_COUPON);
        }
    }

    public void updateIssuedCount() {
        this.issuedCount++;
    }
}
