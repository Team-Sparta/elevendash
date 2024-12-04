package com.example.elevendash.domain.cart.dto.request;

import com.example.elevendash.domain.menu.entity.Menu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartRequestDto {
    private final List<Menu> menuName;

    public CartRequestDto (List<Menu> menuName) {
        this.menuName = new ArrayList<>(menuName);
    }
}
