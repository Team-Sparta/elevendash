package com.example.elevendash.domain.cart.dto.request;

import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.store.entity.Store;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartRequestDto {

    @NotBlank(message = "메뉴를 입력해주세요")
    private final List<String> menus;

    private final List<Long> menuCount;

    private final Long storeId;

    public CartRequestDto (List<String> menus, List<Long> menuCount, Long storeId) {
        this.menus = new ArrayList<>(menus);
        this.menuCount = menuCount;
        this.storeId = storeId;
    }
}
