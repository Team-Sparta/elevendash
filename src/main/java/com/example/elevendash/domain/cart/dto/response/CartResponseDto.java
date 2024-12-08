package com.example.elevendash.domain.cart.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartResponseDto {
    private final Long storeId;

    private final List<String> menus;

    @Min(0)
    private final Integer price;


    public CartResponseDto(Long storeId, List<String> menus, Integer price) {
        this.storeId = storeId;
        this.menus = menus;
        this.price = price;
    }
}
