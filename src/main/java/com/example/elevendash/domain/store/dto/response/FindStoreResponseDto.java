package com.example.elevendash.domain.store.dto.response;

import com.example.elevendash.domain.menu.dto.CategoryInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindStoreResponseDto {
    private final String storeName;
    private final String storeDescription;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final String storeAddress;
    private final String storePhone;
    private final Integer leastAmount;
    private final String storeImage;
    // 메뉴 정보를 담음
    private final List<CategoryInfo> categoryList;
}
