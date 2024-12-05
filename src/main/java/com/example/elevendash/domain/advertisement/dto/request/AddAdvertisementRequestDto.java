package com.example.elevendash.domain.advertisement.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AddAdvertisementRequestDto {
    @NonNull
    @Min(1)
    private final Long storeId;
    @NonNull
    @Min(1)
    private final Integer bidPrice;
}
