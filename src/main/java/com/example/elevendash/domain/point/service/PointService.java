package com.example.elevendash.domain.point.service;

import com.example.elevendash.domain.point.dto.response.TotalPointsResponse;
import com.example.elevendash.domain.point.entity.PointHistory;
import com.example.elevendash.domain.point.enums.PointType;
import com.example.elevendash.domain.point.repository.PointHistoryRepository;
import com.example.elevendash.domain.point.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.elevendash.domain.point.entity.Point;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void addPoints(Long memberId, Long orderId, Integer foodPrice, String description, LocalDateTime expirationAt) {
        Integer calculatedPoint = calculatePoint(foodPrice);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder()
                .memberId(memberId)
                .orderId(orderId)
                .amount(calculatedPoint)
                .expirationDate(now.plusYears(1))
                .build();

        Point savedPoint = pointRepository.save(point);

        PointHistory pointHistory = PointHistory.builder()
                .memberId(memberId)
                .amount(calculatedPoint)
                .point(savedPoint)
                .description(description)
                .type(PointType.EARNED)
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    @Transactional
    public void usePoints(Long memberId, Integer amount, String description, LocalDateTime expirationAt) {

        LocalDateTime now = LocalDateTime.now();

        List<Point> activePoints = pointRepository.findActivePoints(memberId, now);

        int remainingPointsToDeduct = amount;

        for (Point point : activePoints) {

            int pointsAvailable = point.getAmount();
            int pointsToDeduct = Math.min(pointsAvailable, remainingPointsToDeduct);

            point.setAmount(pointsAvailable - pointsToDeduct);

            pointRepository.save(point);

            PointHistory.builder()
                    .memberId(memberId)
                    .amount(pointsToDeduct)
                    .point(point)
                    .description(description)
                    .type(PointType.REDEEMED)
                    .build();

            remainingPointsToDeduct -= pointsToDeduct;

        }
    }


    @Transactional
    public void expirePoints() {
        LocalDateTime now = LocalDateTime.now();
        List<Point> expiringPoints = pointRepository.findByExpirationDateBefore(now);

        pointRepository.deleteAll(expiringPoints);
    }

    @Transactional
    public void rollbackPoints(Long orderId) {
        // Fetch point history for the order where points were used
        List<PointHistory> consumedHistories = pointHistoryRepository.findAll()
                .stream()
                .filter(history -> history.getType() == PointType.REDEEMED)
                .filter(history -> history.getPoint().getOrderId().equals(orderId))
                .toList();

        for (PointHistory consumedHistory : consumedHistories) {
            Point point = consumedHistory.getPoint();
            int restoredPoints = consumedHistory.getAmount();

            // Restore points back to the original record
            point.setAmount(point.getAmount() + restoredPoints);
            pointRepository.save(point);

            // Log restoration in history
            PointHistory history = PointHistory.builder()
                    .memberId(point.getMemberId())
                    .amount(restoredPoints)
                    .point(point)
                    .type(PointType.CANCELLED)
                    .description("Restored previously used points from Order #" + orderId)
                    .build();
            pointHistoryRepository.save(history);
        }
    }

    public TotalPointsResponse getTotalPoints(Long memberId) {
        return pointRepository.getTotalActivePoints(memberId, LocalDateTime.now());
    }

    public Integer calculatePoint(Integer foodPrice) {
        return Math.toIntExact(Math.round(foodPrice * 0.03));
    }

}
