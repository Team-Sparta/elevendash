package com.example.elevendash.domain.order.dto.response;

import lombok.Getter;

@Getter
public class CancelOrderResponseDto {
    private final String orderStatus;

    private final String cancelMessage;

    public CancelOrderResponseDto(String orderStatus, String cancelMessage) {
        this.orderStatus = orderStatus;
        this.cancelMessage = cancelMessage;
    }
}
