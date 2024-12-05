package com.example.elevendash.domain.cart.dto.request;

import com.example.elevendash.domain.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartRequestDto {

    @NotBlank(message = "메뉴를 입력해주세요")
    private final List<Menu> menuName;

    public CartRequestDto (List<Menu> menuName) {
        this.menuName = new ArrayList<>(menuName);
    }
}
