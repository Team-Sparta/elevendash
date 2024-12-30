package com.example.elevendash.global.filter;

import com.example.elevendash.domain.member.dto.AuthUserInfo;
import com.example.elevendash.domain.member.dto.Token;
import com.example.elevendash.domain.member.dto.request.EmailLoginRequest;
import com.example.elevendash.domain.member.dto.response.EmailLoginResponse;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.principal.UserPrincipal;
import com.example.elevendash.global.provider.JwtTokenProvider;
import com.example.elevendash.global.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j(topic = "LoginFilter - 로그인 및 JWT 생성")
@RequiredArgsConstructor
@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    void setup() {
        setFilterProcessesUrl("/members/login/email");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            EmailLoginRequest requestDto = objectMapper.readValue(request.getInputStream(),
                    EmailLoginRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    requestDto.email(),
                    requestDto.password(),
                    null
            );

            return getAuthenticationManager().authenticate(
                    authentication
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info("로그인 성공 및 JWT 생성시작");
        Member member = ((UserPrincipal) authResult.getPrincipal()).member();

        // Access Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(member);

        // 로그인 결과를 담은 DTO 생성
        EmailLoginResponse dto = new EmailLoginResponse(
                new Token(accessToken, jwtTokenProvider.getExpirationByToken(accessToken)),
                new AuthUserInfo(
                        member.getEmail(),
                        member.getProviderId(),
                        member.getProvider()
                )
        );

        // 응답을 JSON으로 직렬화하여 응답 본문에 작성
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 응답 객체를 JSON으로 변환하여 response에 직접 씀
        response.getWriter().write(objectMapper.writeValueAsString(CommonResponse.success(SuccessCode.SUCCESS, dto)));

        // writer를 플러시하여 응답 전송
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}