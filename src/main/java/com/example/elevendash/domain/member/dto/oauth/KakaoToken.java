package com.example.elevendash.domain.member.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoToken(
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("refresh_token_expires_in") int refreshTokenExpiresIn,
        @JsonProperty("scope") String scope
) implements OAuthToken {
}