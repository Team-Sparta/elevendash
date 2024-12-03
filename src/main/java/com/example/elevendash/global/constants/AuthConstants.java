package com.example.elevendash.global.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirectUri";

    public static final String ACCESS_TOKEN_PREFIX = "accessToken:";
    public static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    public static final String TOKEN_PREFIX = "Bearer ";
}