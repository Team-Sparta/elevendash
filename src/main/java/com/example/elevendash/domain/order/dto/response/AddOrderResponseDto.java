package com.example.elevendash.domain.order.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import lombok.Getter;

import java.util.List;

@Getter
public class AddOrderResponseDto {
    private final Long orderId;

    private final Long price;

    private final List<String> menus;


    public AddOrderResponseDto(Long orderId, Long price, List<String> menus) {
        this.orderId = orderId;
        this.price = price;
        this.menus = menus;
    }
}
