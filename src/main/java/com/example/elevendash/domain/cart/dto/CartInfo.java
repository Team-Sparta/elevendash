package com.example.elevendash.domain.cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartInfo {
    private List<CartMenuInfo> cartMenus;

    public CartInfo(){
        cartMenus = new ArrayList<>();
    }
    @JsonCreator
    public CartInfo(@JsonProperty("cartMenus") List<CartMenuInfo> cartMenus){
        this.cartMenus = cartMenus;
    }
}
