package com.example.elevendash.domain.member.service;

import com.example.elevendash.domain.member.controller.SingUpRequest;
import com.example.elevendash.domain.member.dto.response.SignUpResponse;
import com.example.elevendash.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    final MemberRepository memberRepository;

    public SignUpResponse signUp(SingUpRequest request) {

    }
}
