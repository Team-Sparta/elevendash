package com.example.elevendash.domain.member.oauth;

import com.example.elevendash.domain.member.dto.oauth.OAuthToken;
import com.example.elevendash.domain.member.dto.oauth.OAuthUser;

public interface OAuthProvider {

    OAuthToken getOAuthToken(String redirectUri, String code);

    OAuthUser getOAuthUser(String accessToken);
}