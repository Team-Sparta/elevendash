package com.example.elevendash.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank(message = "닉네임이 누락되었습니다.")
        String name
) {
}
