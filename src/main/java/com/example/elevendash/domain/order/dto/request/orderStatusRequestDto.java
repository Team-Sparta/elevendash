package com.example.elevendash.domain.order.dto.request;

import com.example.elevendash.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class orderStatusRequestDto {

    @NotBlank
    private final OrderStatus orderStatus;


    public orderStatusRequestDto(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
