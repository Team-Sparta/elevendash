package com.example.elevendash.domain.menu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor

public class CategoryInfo {
    private final String categoryName;
    private final List<MenuInfoForCategory> menus;
    @Getter
    public static class MenuInfoForCategory {
        private final Long menuId;
        private final String menuName;
        private final String menuDescription;
        private final Integer menuPrice;
        private final String menuImage;

        public MenuInfoForCategory(String menuName, String menuDescription
                , Integer menuPrice
                , String menuImage, Long menuId) {
            this.menuId = menuId;
            this.menuName = menuName;
            this.menuDescription = menuDescription;
            this.menuPrice = menuPrice;
            this.menuImage = menuImage;
        }
    }

}
