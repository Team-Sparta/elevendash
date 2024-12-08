package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class CancelOrderResponseDto {
    private final OrderStatus orderStatus;

    private final String cancelMessage;

    public CancelOrderResponseDto(OrderStatus orderStatus, String cancelMessage) {
        this.orderStatus = orderStatus;
        this.cancelMessage = cancelMessage;
    }
}
