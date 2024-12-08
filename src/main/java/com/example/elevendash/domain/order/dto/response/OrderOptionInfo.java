package com.example.elevendash.domain.order.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderOptionInfo {
    private final Long id;
    private final String name;
    private final Long quantity;
}
