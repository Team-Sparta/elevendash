package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCheckResponseDto {

    private final Long ordersid;

    private final Long price;

    private final List<String> menus;

    private final String status;


    public OrderCheckResponseDto(Long ordersid, Long price, List<String> menus, String status) {
        this.ordersid = ordersid;
        this.price = price;
        this.menus = menus;
        this.status = status;
    }

    public static OrderCheckResponseDto toDto(Order order) {
        return new OrderCheckResponseDto(
                order.getId(),
                order.getPrice(),
                order.getManus(),
                order.getOrderStatus()
        );
    }
}
