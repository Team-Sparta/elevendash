package com.example.elevendash.domain.member.dto.response;

import com.example.elevendash.domain.member.dto.Token;

public record SignUpResponse(
        Long memberId,
        Token accessToken
) {
}
