package com.example.elevendash.domain.member.dto.response;

import com.example.elevendash.domain.member.dto.Token;
import com.example.elevendash.domain.member.dto.AuthUserInfo;

public record EmailLoginResponse(
        Token token,
        AuthUserInfo authUserInfo
) {
}
