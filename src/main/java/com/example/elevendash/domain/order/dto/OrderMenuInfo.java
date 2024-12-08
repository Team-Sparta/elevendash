package com.example.elevendash.domain.order.dto;

import com.example.elevendash.domain.menu.dto.MenuOptionInfo;
import com.example.elevendash.domain.order.dto.response.OrderOptionInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderMenuInfo {
    private final Long menuId;
    private final String menuName;
    private final List<OrderOptionInfo> menuOptions;
}
