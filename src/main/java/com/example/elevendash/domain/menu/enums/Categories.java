package com.example.elevendash.domain.menu.enums;


import lombok.Getter;

@Getter
public enum Categories {
    CHICKEN("치킨"),
    PIZZA("피자"),
    KOREAN_FOOD("한식"),
    CAFE_DESSERT("카페/디저트"),
    CHINESE_FOOD("중식"),
    SNACK_FOOD("분식"),
    SANDWICH("샌드위치"),
    BURGER("버거"),
    SUSHI("초밥"),
    JAPANESE_FOOD("일식"),
    ASIAN_FOOD("아시안"),
    MEAT_DISH("고기/구이"),
    STEAMED_FOOD("찜/탕"),
    LUNCH_BOX("도시락"),
    PORRIDGE("죽");

    private final String koreanName;

    Categories(String koreanName) {
        this.koreanName = koreanName;
    }

}

