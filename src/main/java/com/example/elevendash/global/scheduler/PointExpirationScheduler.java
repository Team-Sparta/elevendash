package com.example.elevendash.global.scheduler;

import com.example.elevendash.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointExpirationScheduler {

    private final PointService pointService;

    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    public void expirePoints() {
        pointService.expirePoints();
    }
}