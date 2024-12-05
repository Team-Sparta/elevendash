package com.example.elevendash.domain.coupon.entity;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.entity.BaseCreatedTimeEntity;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "coupon_usage"
)
public class CouponUsage extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '쿠폰 사영 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '쿠폰 고유 번호'")
    private Coupon coupon;

    private boolean used; // 쿠폰 사용 여부

    public CouponUsage(Coupon coupon, Member member, boolean used) {
        this.coupon = coupon;
        this.member = member;
        this.used = used;
    }

    public void validate() {
        if (this.getCoupon().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BaseException(ErrorCode.EXPIRED_COUPON);
        }

        if (this.isUsed()) {
            throw new BaseException(ErrorCode.ALREADY_USED_COUPON);
        }
    }

    public void useCoupon() {
        this.used = true;
    }
}
