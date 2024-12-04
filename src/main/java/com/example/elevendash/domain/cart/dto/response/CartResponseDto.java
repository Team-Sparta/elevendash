package com.example.elevendash.domain.cart.dto.response;

import com.example.elevendash.domain.menu.entity.Menu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartResponseDto {

    private final Long id;

    private final List<Menu> manuName;

    public CartResponseDto (Long id, List<Menu> manuName) {
        this.id = id;
        this.manuName = new ArrayList<>(manuName);
    }


}
