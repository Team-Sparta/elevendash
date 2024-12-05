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

    private final List<Long> menuId;

    @Min(0)
    private final Long price;

    public CartResponseDto (List<Long> menuId, Long price) {
        this.menuId = new ArrayList<>(menuId);
        this.price = price;
    }


}
