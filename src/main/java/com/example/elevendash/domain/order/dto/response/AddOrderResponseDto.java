package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
            import com.example.elevendash.domain.order.entity.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class AddOrderResponseDto {
    private final Long orderId;

    private final String orderStatus;

    private final Long price;

    private final List<Menu> menus;


    public AddOrderResponseDto(Long orderId, String orderStatus, Long price, List<Menu> menus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.price = price;
        this.menus = List.copyOf(menus);
    }



    public static AddOrderResponseDto toDto(Order order) {
        return new AddOrderResponseDto(
        order.getId(),
        order.getOrderStatus(),
        order.getPrice(),
        order.getMenu()
        );
    }
}
