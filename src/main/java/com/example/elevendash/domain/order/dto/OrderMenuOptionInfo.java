package com.example.elevendash.domain.order.dto;

import com.example.elevendash.domain.menu.entity.MenuOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderMenuOptionInfo {
    private final MenuOption menuOption;
    private final Long quantity;
}
