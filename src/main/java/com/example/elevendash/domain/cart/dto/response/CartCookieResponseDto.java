package com.example.elevendash.domain.cart.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartCookieResponseDto {

    @NotBlank(message = "메뉴를 입력해주세요")
    private final List<Menu> menus;

    private final List<Long> menuCount;

    private final Long storeId;


    public CartCookieResponseDto(List<Menu> menus, List<Long> menuCount, Long storeId) {
        this.menus = menus;
        this.menuCount = menuCount;
        this.storeId = storeId;
    }
}
