package com.example.elevendash.domain.member.client;

import com.example.elevendash.domain.member.dto.oauth.kakao.KakaoUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakao-api", url = "${api.kakao.kapi}")
public interface KakaoApiClient {

    @GetMapping("/v2/user/me")
    KakaoUser getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}