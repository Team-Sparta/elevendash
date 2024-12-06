package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class OrderCheckResponseDto {

    private final Long ordersid;

    private final Long price;

    private final List<Menu> menu;

    private final String status;


    public OrderCheckResponseDto(Long ordersid, Long price, List<Menu> menu, String status) {
        this.ordersid = ordersid;
        this.price = price;
        this.menu = menu;
        this.status = status;
    }

    public static OrderCheckResponseDto toDto(Order order) {
        return new OrderCheckResponseDto(
                order.getId(),
                order.getPrice(),
                order.getMenu(),
                order.getOrderStatus()
        );
    }
}
