package com.example.elevendash.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class orderStatusRequestDto {

    @NotBlank
    private final String orderStatus;




    public orderStatusRequestDto(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
