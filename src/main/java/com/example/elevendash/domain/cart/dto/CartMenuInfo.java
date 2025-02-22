package com.example.elevendash.domain.cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartMenuInfo {
    @NotNull
    @Min(1)
    private final Long menuId;
    @NotNull
    @Min(1)
    private final Long storeId;

    private final List<MenuOptionInfo> menuOptions;
    @JsonCreator
    public CartMenuInfo(@JsonProperty("menuId") Long menuId, @JsonProperty("storeId") Long storeId,@JsonProperty("menuOptions") List<MenuOptionInfo> menuOptions) {
        this.menuId = menuId;
        this.storeId = storeId;
        this.menuOptions = menuOptions;
    }


    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MenuOptionInfo {
        private final Long optionId;
        private final Long optionQuantity;
        @JsonCreator
        MenuOptionInfo(@JsonProperty("optionId") Long optionId, @JsonProperty("optionQuantity") Long optionQuantity){
            this.optionId = optionId;
            this.optionQuantity = optionQuantity;
        }

    }
}
