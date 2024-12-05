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
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(20) COMMENT 'Type of Coupon (e.g., Fixed or Percentage)'")
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(name = "discount_value", nullable = false, columnDefinition = "DECIMAL(10,2) COMMENT '할인 값 (fixed amount or percentage)'")
    private BigDecimal discountValue;

    @Column(name = "max_discount_amount", nullable = true, columnDefinition = "DECIMAL(10,2) COMMENT '쿠폰의 최대 할인 금액'")
    private BigDecimal maxDiscountAmount;

    @Column(name = "expiration_date", nullable = false, columnDefinition = "DATETIME COMMENT '쿠폰 만료 일자'")
    private LocalDateTime expirationDate;

    @Column(name = "total_issued_count", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '총 발급 가능 쿠폰 수'")
    private Integer totalIssuedCount;

    @Column(name = "daily_issued_count_limit", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '하루 발급 가능 한도'")
    private Integer dailyIssuedCountLimit;

    @Column(name = "issued_count", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0 COMMENT '현재까지 발급된 쿠폰 수'")
    private Integer issuedCount = 0;

    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, orphanRemoval = true)
    List<CouponUsage> couponUsages = new ArrayList<>();

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
            throw new BaseException(ErrorCode.DAILY_OVER_ISSUED_COUPON);
        }
    }

    public void updateIssuedCount() {
        this.issuedCount++;
    }
}
