package com.example.elevendash.domain.member.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
        @JsonProperty("has_email")boolean hasEmail,
        @JsonProperty("email_needs_agreement") String emailNeedsAgreement,
        @JsonProperty("is_email_valid") String isEmailValid,
        @JsonProperty("is_email_verified") String isEmailVerified,
        @JsonProperty("email") String email
) {
}