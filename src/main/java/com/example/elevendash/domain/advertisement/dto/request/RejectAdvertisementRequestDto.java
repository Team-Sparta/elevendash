package com.example.elevendash.domain.advertisement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RejectAdvertisementRequestDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private final String rejectReason;

    private RejectAdvertisementRequestDto() {
        this.rejectReason = "거절";
    }
}
