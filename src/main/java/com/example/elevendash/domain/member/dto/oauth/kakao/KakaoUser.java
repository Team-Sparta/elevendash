package com.example.elevendash.domain.member.dto.oauth.kakao;

import com.example.elevendash.domain.member.dto.oauth.OAuthUser;
import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUser(
        @JsonProperty("id") String id,
        @JsonProperty("has_signed_up") String hasSignedUp,
        @JsonProperty("connected_at") String connectedAt,
        @JsonProperty("synched_at") String synchedAt,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) implements OAuthUser {
    @Override
    public String email() {
        return kakaoAccount.email();
    }
}