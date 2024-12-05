package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse;
import com.example.elevendash.domain.dashboard.enums.PeriodType;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderService) {
        this.orderRepository = orderService;
    }

    public Order findOrderById(Long ordersId) {
        return orderRepository.findById(ordersId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
    }

    public OrderCheckResponseDto orderDetails(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getOrderStatus().equals("주문 거절")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다");
        }
        return OrderCheckResponseDto.toDto(findOrderById(orderId));
    }

    public StatisticsResponse getStatistics(PeriodType periodType, LocalDate startDate, LocalDate endDate, Long storeId, Categories categories) {
        LocalDate now = LocalDate.now();

        switch (periodType) {
            case DAILY -> {
                startDate = now.minusDays(1);
                endDate = now;
            }
            case MONTHLY -> {
                startDate = now.minusMonths(1);
                endDate = now;
            }
            case ANNUAL -> {
                startDate = now.minusYears(1);
                endDate = now;
            }
        }

        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        // Convert LocalDate to LocalDateTime at the end of the day
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        return orderRepository.getStatistics(startDateTime, endDateTime, storeId);
    }
}
