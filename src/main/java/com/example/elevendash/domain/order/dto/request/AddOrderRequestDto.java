package com.example.elevendash.domain.order.dto.request;

import lombok.Getter;

@Getter
public class AddOrderRequestDto {

    private final Long pointId;

    private final Long couponId;

    public AddOrderRequestDto( Long point, Long couponId) {
        this.pointId = point;
        this.couponId = couponId;
    }
}
