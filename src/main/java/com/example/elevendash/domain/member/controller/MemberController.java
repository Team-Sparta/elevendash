package com.example.elevendash.domain.member.controller;

import com.example.elevendash.domain.advertisement.dto.request.UpdateAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.FindAllMyAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.dto.response.UpdateAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.service.AdvertisementService;
import com.example.elevendash.domain.coupon.dto.response.CouponListResponse;
import com.example.elevendash.domain.coupon.service.CouponService;
import com.example.elevendash.domain.member.dto.response.*;
import com.example.elevendash.domain.member.dto.request.EmailLoginRequest;
import com.example.elevendash.domain.member.dto.request.OAuthLoginRequest;
import com.example.elevendash.domain.member.dto.request.SignUpRequest;
import com.example.elevendash.domain.member.dto.request.UpdateProfileRequest;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.service.MemberService;
import com.example.elevendash.domain.point.dto.response.TotalPointsResponse;
import com.example.elevendash.domain.point.service.PointService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PointService pointService;
    private final CouponService couponService;
    private final AdvertisementService advertisementService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<SignUpResponse>> singUp(@Valid @RequestBody SignUpRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, memberService.signUp(request));
    }

    @PostMapping("/login/email")
    public ResponseEntity<CommonResponse<EmailLoginResponse>> emailLogin(@Valid @RequestBody EmailLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.emailLogin(request));
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<CommonResponse<OAuthLoginResponse>> kakaoLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.oAuthLogin(request));
    }

    @PostMapping("/login/naver")
    public ResponseEntity<CommonResponse<OAuthLoginResponse>> naverLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.oAuthLogin(request));
    }

    @GetMapping("/{memberId}/profile")
    public ResponseEntity<CommonResponse<MemberProfileResponse>> getMemberProfile(
            @PathVariable Long memberId,
            @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMemberProfile(memberId));
    }

    @PutMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
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

    @GetMapping("/my/my-stores")
    public ResponseEntity<CommonResponse<FindMyStoreResponseDto>> findMyStores(
            @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.findMyStores(loginMember));
    }

    @GetMapping("my/my-advertisements")
    public ResponseEntity<CommonResponse<FindAllMyAdvertisementResponseDto>> findMyAdvertisements(
            @LoginMember Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS, advertisementService.findAllMyAdvertisement(loginMember));
    }

    @GetMapping("my/my-advertisements/{advertisementId}")
    public ResponseEntity<CommonResponse<UpdateAdvertisementResponseDto>> updateAdvertisement(
            @RequestBody @Valid UpdateAdvertisementRequestDto requestDto,
            @PathVariable Long advertisementId,
            @LoginMember Member loginMember
            ){
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE,advertisementService.updateAdvertisement(
                loginMember,advertisementId,requestDto));
    }

    @GetMapping("/my/points")
    public ResponseEntity<CommonResponse<TotalPointsResponse>> getPoints(
            @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, pointService.getTotalPoints(loginMember.getId()));
    }

    @GetMapping("/my/coupons")
    public ResponseEntity<CommonResponse<CouponListResponse>> getCoupons(
            @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, couponService.getMyCoupons(loginMember.getId()));
    }
}
