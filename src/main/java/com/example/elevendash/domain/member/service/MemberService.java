package com.example.elevendash.domain.member.service;

import com.example.elevendash.domain.member.dto.Token;
import com.example.elevendash.domain.member.dto.request.SignUpRequest;
import com.example.elevendash.domain.member.dto.response.SignUpResponse;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.global.exception.InvalidParamException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Validated
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordService passwordService;

    public SignUpResponse signUp(@Validated SignUpRequest request) {
        validateEmail(request.email());

        Member member = Member.builder().signUpRequest(request).build();

        member.setPassword(passwordService.encode(request.password()));

        memberRepository.save(member);

        String token = jwtTokenProvider.createAccessToken(createClaims(member));

        return new SignUpResponse(
                member.getId(),
                new Token(token, jwtTokenProvider.getExpirationByToken(token))
        );
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new InvalidParamException(ErrorCode.EXIST_EMAIL);
        }
    }

    private HashMap<String, Object> createClaims(Member member) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", member.getId());
        hashMap.put("name", member.getName());
        hashMap.put("email", member.getEmail());
        return hashMap;
    }
}
