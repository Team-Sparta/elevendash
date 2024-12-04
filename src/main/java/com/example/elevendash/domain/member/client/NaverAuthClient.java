package com.example.elevendash.domain.member.client;

import com.example.elevendash.domain.member.dto.oauth.naver.NaverToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "naver-auth", url = "${api.naver.nauth}")
public interface NaverAuthClient {
    @PostMapping(value = "/oauth2.0/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    NaverToken generateOAuthToken(@RequestParam(name = "code") String code,
                                  @RequestParam(name = "client_id") String clientId,
                                  @RequestParam(name = "client_secret") String clientSecret,
                                  @RequestParam(name = "grant_type") String grantType);

}
