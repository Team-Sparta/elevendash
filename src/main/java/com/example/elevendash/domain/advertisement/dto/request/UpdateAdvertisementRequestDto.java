package com.example.elevendash.domain.advertisement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateAdvertisementRequestDto {
    @NotNull
    @Min(1)
    private final Integer bidPrice;
}
