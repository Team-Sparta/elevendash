package com.example.elevendash.domain.advertisement.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RejectAdvertisementRequestDto {
    private final String rejectReason;

    private RejectAdvertisementRequestDto() {
        rejectReason = null;
    }
}
