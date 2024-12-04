package com.example.elevendash.domain.member.dto;

import com.example.elevendash.domain.member.enums.Provider;

public record AuthUserInfo(
        String email,
        String providerId,
        Provider provider
) {
}
