package com.example.elevendash.domain.store.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreInfo {
    private final Long storeId;
    private final Integer bookMarkCount;
    private final String storeName;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final String storeAddress;
    private final Integer leastAmount;

}
