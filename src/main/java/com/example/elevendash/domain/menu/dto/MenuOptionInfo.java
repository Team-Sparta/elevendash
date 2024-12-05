package com.example.elevendash.domain.menu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuOptionInfo {
    private final Long optionId;
    private final String content;
    private final Integer optionPrice;
}
