package com.example.elevendash.domain.member.client;

import com.example.elevendash.domain.member.dto.oauth.kakao.KakaoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakao-auth", url = "${api.kakao.kauth}")
public interface KakaoAuthClient {

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoToken generateOAuthToken(@RequestParam(name = "grant_type") String grantType,
                                  @RequestParam(name = "client_id") String clientId,
                                  @RequestParam(name = "redirect_uri") String redirectUri,
                                  @RequestParam(name = "code") String code,
                                  @RequestParam(name = "client_secret") String clientSecret);
}