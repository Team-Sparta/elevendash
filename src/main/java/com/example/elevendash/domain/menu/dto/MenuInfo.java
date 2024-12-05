package com.example.elevendash.domain.menu.dto;

import com.example.elevendash.domain.menu.entity.MenuOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MenuInfo {
    private final String menuName;
    private final String menuDescription;
    private final Integer menuPrice;
    private final String menuImage;

    private final List<MenuOptionInfo> menuOptions;
}
