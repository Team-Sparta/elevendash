package com.example.elevendash.domain.member.dto.oauth.naver;

import com.example.elevendash.domain.member.dto.oauth.OAuthUser;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverUser(
        @JsonProperty("response") NaverAccount naverAccount
) implements OAuthUser {
    @Override
    public String id() {
        return naverAccount.id();
    }

    @Override
    public String email() {
        return naverAccount.email();
    }
}
