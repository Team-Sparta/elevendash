package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.menu.dto.response.FindMenuResponseDto;
import com.example.elevendash.domain.order.dto.OrderMenuInfo;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.entity.OrderMenu;
import com.example.elevendash.domain.order.enums.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderCheckResponseDto {

    private final Long orderId;

    private final BigDecimal price;

    private final List<OrderMenuInfo> orderMenuInfoList;

    private final OrderStatus status;

}
