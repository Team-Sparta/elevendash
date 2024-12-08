package com.example.elevendash.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddOrderRequestDto {
    @NotNull
    private final Long pointId;
    @NotNull
    private final Long couponId;

    public AddOrderRequestDto( Long pointId, Long couponId) {
        this.pointId = pointId;
        this.couponId = couponId;
    }
}
