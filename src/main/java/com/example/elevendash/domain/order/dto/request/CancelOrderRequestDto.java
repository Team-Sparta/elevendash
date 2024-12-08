package com.example.elevendash.domain.order.dto.request;

import lombok.Getter;

@Getter
public class CancelOrderRequestDto {

    private final String cancelMassage;

    public CancelOrderRequestDto( String cancelMassage) {
        this.cancelMassage = cancelMassage;
    }
}
