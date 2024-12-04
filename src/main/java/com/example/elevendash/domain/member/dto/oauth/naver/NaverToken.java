package com.example.elevendash.domain.member.dto.oauth.naver;

import com.example.elevendash.domain.member.dto.oauth.OAuthToken;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverToken(
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("error") String error,
        @JsonProperty("error_description") String errorDescription
) implements OAuthToken {
    @Override
    public String scope() {
        return "account_email";
    }
}