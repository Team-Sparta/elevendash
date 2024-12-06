package com.example.elevendash.domain.order.dto.request;

import lombok.Getter;

@Getter
public class AddOrderRequestDto {
    private final Long memberId;

    private final Long point;

    private final Long couponId;

    public AddOrderRequestDto(Long memberId, Long point, Long couponId) {
        this.memberId = memberId;
        this.point = point;
        this.couponId = couponId;
    }
}
