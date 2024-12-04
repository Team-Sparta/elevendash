package com.example.elevendash.domain.member.dto.oauth.naver;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverAccount(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email
) {
}
