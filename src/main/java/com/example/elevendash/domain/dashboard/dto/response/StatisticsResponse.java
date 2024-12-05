package com.example.elevendash.domain.dashboard.dto.response;

public record StatisticsResponse(
        Integer totalOrders,
        Integer totalAmount
) {
}
