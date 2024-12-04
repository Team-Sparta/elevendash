package com.example.elevendash.domain.member.oauth;

import com.example.elevendash.domain.member.client.KakaoApiClient;
import com.example.elevendash.domain.member.client.KakaoAuthClient;
import com.example.elevendash.domain.member.dto.oauth.OAuthToken;
import com.example.elevendash.domain.member.dto.oauth.OAuthUser;
import com.example.elevendash.global.constants.AuthConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoProvider implements OAuthProvider {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthClient kakaoAuthClient;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @CircuitBreaker(name = "kakaoApi", fallbackMethod = "getOAuthTokenFallback")
    @Override
    public OAuthToken getOAuthToken(String redirectUri, String code) {
        return kakaoAuthClient.generateOAuthToken(
                grantType,
                clientId,
                redirectUri,
                code,
                clientSecret
        );
    }

    @CircuitBreaker(name = "kakaoApi", fallbackMethod = "getOAuthTokenFallback")
    @Override
    public OAuthUser getOAuthUser(String accessToken) {
        return kakaoApiClient.getUserInfo(getBearerToken(accessToken));
    }

    private String getBearerToken(String accessToken) {
        return AuthConstants.TOKEN_PREFIX + accessToken;
    }

    public void getOAuthTokenFallback(String redirectUri, String code, Throwable ex) {
        log.error("Failed to fetch OAuth token from Kakao API", ex);
    }

    public void getOAuthUserFallback(String redirectUri, String code, Throwable ex) {
        log.error("Failed to fetch User from Kakao API", ex);
    }


}