package com.example.elevendash.domain.member.dto.response;

import com.example.elevendash.domain.member.dto.AuthUserInfo;
import com.example.elevendash.domain.member.dto.Token;

public record OAuthLoginResponse(
        Token token,
        boolean isNewMember,
        AuthUserInfo oAuthUserInfo
) {
}
