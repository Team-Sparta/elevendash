package com.example.elevendash.domain.cart.dto.request;

import com.example.elevendash.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartRequestDto {

    private String menuName;

    private Long price;

    private Long quantity;

    private Long storeId;

}
