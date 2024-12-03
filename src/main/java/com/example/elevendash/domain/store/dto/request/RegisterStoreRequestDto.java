package com.example.elevendash.domain.store.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterStoreRequestDto {
    private final String storeName;
    private final String storeDescription;
    private final String storeAddress;
    private final String storePhone;
    private final Integer leastAmount;
}
