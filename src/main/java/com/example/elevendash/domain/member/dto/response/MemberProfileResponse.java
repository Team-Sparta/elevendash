package com.example.elevendash.domain.member.dto.response;

public record MemberProfileResponse(
        Long memberId,
        String name,
        String profileImage
) {
}
