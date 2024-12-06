package com.example.elevendash.domain.order.dto.request;

import lombok.Getter;

@Getter
public class CancelOrderRequestDto {
    private final String orderStatus;

    private final String cancelMassage;

    public CancelOrderRequestDto(String orderStatus, String cancelMassage) {
        this.orderStatus = orderStatus;
        this.cancelMassage = cancelMassage;
    }
}
