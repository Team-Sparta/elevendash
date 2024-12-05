package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderCheckResponseDto {

    private final Long ordersid;

    private final Long price;

    private final String menuName;

    private final String status;


    public OrderCheckResponseDto(Long ordersid, Long price, String menuName, String status) {
        this.ordersid = ordersid;
        this.price = price;
        this.menuName = menuName;
        this.status = status;
    }

    public static OrderCheckResponseDto toDto(Order order) {
        return new OrderCheckResponseDto(
                order.getId(),
                order.getPrice(),
                order.getManuName(),
                order.getOrderStatus()
        );
    }
}
