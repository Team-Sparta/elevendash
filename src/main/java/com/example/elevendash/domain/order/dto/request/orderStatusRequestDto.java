package com.example.elevendash.domain.order.dto.request;

import com.example.elevendash.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class orderStatusRequestDto {

    @NotNull
    private final OrderStatus orderStatus;


    public orderStatusRequestDto(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public orderStatusRequestDto() {
        this.orderStatus = null;
    }
}
