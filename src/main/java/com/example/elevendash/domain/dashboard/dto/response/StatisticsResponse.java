package com.example.elevendash.domain.dashboard.dto.response;

import java.math.BigDecimal;

public record StatisticsResponse(Long totalOrders, BigDecimal totalAmount) {
}