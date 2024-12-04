package com.example.elevendash.domain.member.controller;

import com.example.elevendash.domain.member.dto.MemberProfileResponse;
import com.example.elevendash.domain.member.dto.request.EmailLoginRequest;
import com.example.elevendash.domain.member.dto.request.SignUpRequest;
import com.example.elevendash.domain.member.dto.request.UpdateProfileRequest;
import com.example.elevendash.domain.member.dto.response.EmailLoginResponse;
import com.example.elevendash.domain.member.dto.response.SignUpResponse;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.service.MemberService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<SignUpResponse>> singUp(@Valid @RequestBody SignUpRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, memberService.signUp(request));
    }

    @PostMapping("/login/email")
    public ResponseEntity<CommonResponse<EmailLoginResponse>> emailLogin(@Valid @RequestBody EmailLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.emailLogin(request));
    }

    @GetMapping("/{memberId}/profile")
    public ResponseEntity<CommonResponse<MemberProfileResponse>> getMemberProfile(
            @PathVariable Long memberId,
            @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMemberProfile(memberId, loginMember));
    }

    @PutMapping
    public ResponseEntity<CommonResponse<UpdateProfileResponse>> updateProfile(
            @LoginMember Member loginMember,
            @Valid @RequestPart(value = "request") UpdateProfileRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.updateProfile(loginMember, request, image));
    }


    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteMember(
            @LoginMember Member loginMember
    ) {
        memberService.deleteMember(loginMember);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }


}
