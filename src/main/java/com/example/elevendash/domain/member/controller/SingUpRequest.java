package com.example.elevendash.domain.member.controller;

import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.member.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SingUpRequest(

        @NotBlank(message = "닉네임이 누락되었습니다.")
        String name,

        @Email(message = "이메일 주소 형식이 잘못되었습니다.")
        String email,

        String password,

        @NotNull(message = "가입경로가 누락되었습니다.")
        Provider provider,

        String providerId,

        MemberRole role
) {
}
