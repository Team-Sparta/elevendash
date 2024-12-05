package com.example.elevendash.domain.advertisement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteAdvertisementResponseDto {
    private final Long advertisementId;
}
