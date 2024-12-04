package com.example.elevendash.domain.member.oauth;

import com.example.elevendash.domain.member.client.NaverApiClient;
import com.example.elevendash.domain.member.client.NaverAuthClient;
import com.example.elevendash.domain.member.dto.oauth.OAuthToken;
import com.example.elevendash.domain.member.dto.oauth.OAuthUser;
import com.example.elevendash.global.constants.AuthConstants;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverProvider implements OAuthProvider {

    private final NaverApiClient naverApiClient;
    private final NaverAuthClient naverAuthClient;

    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @CircuitBreaker(name = "naverApi", fallbackMethod = "getOAuthTokenFallback")
    @Override
    public OAuthToken getOAuthToken(String redirectUri, String code) {
        return naverAuthClient.generateOAuthToken(
                code,
                clientId,
                clientSecret,
                grantType
        );
    }

    @CircuitBreaker(name = "naverApi", fallbackMethod = "getOAuthUserFallback")
    @Override
    public OAuthUser getOAuthUser(String accessToken) {
        return naverApiClient.getUserInfo(getBearerToken(accessToken));
    }

    private String getBearerToken(String accessToken) {
        return AuthConstants.TOKEN_PREFIX + accessToken;
    }

    public OAuthToken getOAuthTokenFallback(String redirectUri, String code, Throwable ex) {
        log.error("Failed to fetch OAuth token from Naver API", ex);
        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public OAuthUser getOAuthUserFallback(String accessToken, Throwable ex) {
        log.error("Failed to fetch User from Naver API", ex);
        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
