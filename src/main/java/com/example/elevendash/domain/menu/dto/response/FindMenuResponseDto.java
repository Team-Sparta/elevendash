package com.example.elevendash.domain.menu.dto.response;

import com.example.elevendash.domain.menu.dto.MenuOptionInfo;
import com.example.elevendash.domain.menu.enums.Categories;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindMenuResponseDto {
    private final Long menuId;
    private final String menuName;
    private final Categories menuCategory;
    private final String menuImage;
    private final String menuDescription;
    private final Integer menuPrice;
    private final List<MenuOptionInfo> menuOptions;

}
