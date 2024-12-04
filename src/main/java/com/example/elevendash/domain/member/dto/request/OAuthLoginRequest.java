package com.example.elevendash.domain.member.dto.request;

import com.example.elevendash.domain.member.dto.OAuthLoginInfo;
import com.example.elevendash.domain.member.enums.Provider;
import com.example.elevendash.global.constants.AuthConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record OAuthLoginRequest(

        @NotBlank(message = "인가코드가 누락되었습니다.")
        String code,

        @NotNull(message = "리다이렉트 URI가 누락되었습니다.")
        String redirectUri,

        @NotNull(message = "가입 경로가 누락되었습니다.")
        Provider provider
) {
    public OAuthLoginInfo toDto() {
        Map<String, String> propertyMap = Map.of(
                AuthConstants.CODE, code,
                AuthConstants.REDIRECT_URI, redirectUri
        );
        return new OAuthLoginInfo(provider, propertyMap);
    }
}
