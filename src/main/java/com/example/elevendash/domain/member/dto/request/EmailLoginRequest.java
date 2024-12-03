package com.example.elevendash.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailLoginRequest(
        @Email(message = "이메일 주소 형식이 잘못되었습니다.")
        String email,

        @NotEmpty(message = "비밀번호가 생락되었습니다.")
        String password
) {
}
