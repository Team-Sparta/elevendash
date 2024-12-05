package com.example.elevendash.global.scheduler;

import com.example.elevendash.domain.coupon.entity.Coupon;
import com.example.elevendash.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponScheduler {

    private final CouponRepository couponRepository;

    @Scheduled(cron = "0 0 2 * * ?") // 매일 오전 2시
    public void deleteExpiredCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<Coupon> expiredCoupons = couponRepository.findAllByExpirationDateBefore(now);

        if (!expiredCoupons.isEmpty()) {
            couponRepository.deleteAll(expiredCoupons);
            log.info("{} 의 쿠폰이 삭제되었습니다.", expiredCoupons.size());
        } else {
            log.info("만료기간이 지난 쿠폰이 없습니다.");
        }
    }

}
