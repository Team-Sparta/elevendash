package com.example.elevendash.domain.member.dto.oauth;

public interface OAuthToken {
    String tokenType();

    String accessToken();

    int expiresIn();

    String refreshToken();

    String scope();

    int refreshTokenExpiresIn();
}