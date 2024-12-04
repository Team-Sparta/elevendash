package com.example.elevendash.domain.member.dto;

public record MemberProfileResponse(
        Long memberId,
        String name,
        String profileImage
) {
}
