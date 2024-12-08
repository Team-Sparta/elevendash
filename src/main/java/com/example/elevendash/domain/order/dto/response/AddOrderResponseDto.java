package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.entity.OrderMenu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AddOrderResponseDto {
    private final Long orderId;
}
