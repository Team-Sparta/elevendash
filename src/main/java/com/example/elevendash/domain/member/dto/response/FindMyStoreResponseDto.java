package com.example.elevendash.domain.member.dto.response;

import com.example.elevendash.domain.store.dto.StoreInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class FindMyStoreResponseDto {
    private final List<StoreInfo> myStoreInfoList;
}
