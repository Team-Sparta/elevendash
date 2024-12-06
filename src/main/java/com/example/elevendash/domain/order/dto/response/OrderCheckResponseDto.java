package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.entity.OrderItems;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class OrderCheckResponseDto {

    private final Long ordersid;

    private final Long price;

    private final List<OrderItems> orderItems;

    private final String status;


    public OrderCheckResponseDto(Long ordersid, Long price, List<OrderItems> orderItems, String status) {
        this.ordersid = ordersid;
        this.price = price;
        this.orderItems = orderItems;
        this.status = status;
    }

    public static OrderCheckResponseDto toDto(Order order) {
        return new OrderCheckResponseDto(
                order.getId(),
                order.getPrice(),
                order.getOrderItems(),
                order.getOrderStatus()
        );
    }
}
