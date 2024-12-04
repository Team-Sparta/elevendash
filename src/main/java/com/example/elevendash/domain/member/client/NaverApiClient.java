package com.example.elevendash.domain.member.client;

import com.example.elevendash.domain.member.dto.oauth.naver.NaverUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "naver-api", url = "${api.naver.napi}")
public interface NaverApiClient {
    @GetMapping("/v1/nid/me")
    NaverUser getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
