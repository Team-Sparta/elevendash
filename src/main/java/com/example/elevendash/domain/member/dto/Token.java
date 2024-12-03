package com.example.elevendash.domain.member.dto;

import java.util.Date;

public record Token(
        String token,
        Date expiredAt
) {
}
