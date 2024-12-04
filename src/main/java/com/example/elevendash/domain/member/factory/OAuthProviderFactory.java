package com.example.elevendash.domain.member.factory;

import com.example.elevendash.domain.member.dto.oauth.OAuthToken;
import com.example.elevendash.domain.member.dto.oauth.OAuthUser;
import com.example.elevendash.domain.member.enums.Provider;
import com.example.elevendash.domain.member.oauth.KakaoProvider;
import com.example.elevendash.domain.member.oauth.OAuthProvider;
import com.example.elevendash.global.exception.InvalidParamException;
import com.example.elevendash.global.exception.code.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component
public class OAuthProviderFactory {

    private final Map<Provider, OAuthProvider> OAuthProviderMap;
    private final KakaoProvider kakaoProvider;

    public OAuthProviderFactory(
            KakaoProvider kakaoProvider
    ) {
        OAuthProviderMap = new EnumMap<>(Provider.class);
        this.kakaoProvider = kakaoProvider;
        initialize();
    }

    private void initialize() {
        OAuthProviderMap.put(Provider.KAKAO, kakaoProvider);
    }

    private OAuthProvider getOAuthProvider(Provider provider) {
        OAuthProvider oAuthProvider = OAuthProviderMap.get(provider);
        if (Objects.isNull(oAuthProvider)) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_OAUTH_PROVIDER);
        }

        return oAuthProvider;
    }

    public String getAccessToken(Provider provider, String redirectUri, String code) {
        return getOAuthToken(provider, redirectUri, code).accessToken();
    }

    private OAuthToken getOAuthToken(Provider provider, String redirectUri, String code) {
        return getOAuthProvider(provider).getOAuthToken(redirectUri, code);
    }

    public String getProviderId(OAuthUser oAuthUser) {
        return oAuthUser.id();
    }

    public String getEmail(OAuthUser oAuthUser) {
        return oAuthUser.email();
    }

    public OAuthUser getOAuthUser(Provider provider, String accessToken) {
        return getOAuthProvider(provider).getOAuthUser(accessToken);
    }

}