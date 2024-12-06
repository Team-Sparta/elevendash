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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "회원 API",
        description = "회원 관련 API"
)
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PointService pointService;
    private final CouponService couponService;
    private final AdvertisementService advertisementService;

    @Operation(
            summary = "회원 가입",
            description = "회원 가입을 진행한다. (이메일이 아닌 다른 경로를 통한 가입 시 providerId 추가)",
            tags = {"회원 API"}
    )
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, memberService.signUp(request));
    }

    @Operation(
            summary = "이메일 로그인",
            description = "이메일과 비밀번호로 로그인한다.",
            tags = {"회원 API"}
    )
    @PostMapping("/login/email")
    public ResponseEntity<CommonResponse<EmailLoginResponse>> emailLogin(@Valid @RequestBody EmailLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.emailLogin(request));
    }

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 계정을 통해 로그인한다.",
            tags = {"회원 API"}
    )
    @PostMapping("/login/kakao")
    public ResponseEntity<CommonResponse<OAuthLoginResponse>> kakaoLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.oAuthLogin(request));
    }

    @Operation(
            summary = "네이버 로그인",
            description = "네이버 계정을 통해 로그인한다.",
            tags = {"회원 API"}
    )
    @PostMapping("/login/naver")
    public ResponseEntity<CommonResponse<OAuthLoginResponse>> naverLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.oAuthLogin(request));
    }

    @Operation(
            summary = "회원 프로필 조회",
            description = "특정 회원의 프로필을 조회한다.",
            tags = {"회원 API"}
    )
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<CommonResponse<MemberProfileResponse>> getMemberProfile(
            @PathVariable Long memberId,
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.getMemberProfile(memberId));
    }

    @Operation(
            summary = "회원 프로필 수정",
            description = "회원 프로필 및 프로필 이미지를 수정한다.",
            tags = {"회원 API"}
    )
    @PutMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<UpdateProfileResponse>> updateProfile(
            @Valid @RequestPart(value = "request") UpdateProfileRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.updateProfile(loginMember, request, image));
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "로그인된 회원 정보를 삭제한다.",
            tags = {"회원 API"}
    )
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteMember(
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        memberService.deleteMember(loginMember);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

    @Operation(
            summary = "내 매장 조회",
            description = "로그인된 회원이 등록한 매장을 조회한다.",
            tags = {"회원 API"}
    )
    @GetMapping("/my/my-stores")
    public ResponseEntity<CommonResponse<FindMyStoreResponseDto>> findMyStores(
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberService.findMyStores(loginMember));
    }

    @Operation(
            summary = "내 광고 조회",
            description = "로그인된 회원이 등록한 모든 광고를 조회한다.",
            tags = {"회원 API"}
    )
    @GetMapping("/my/my-advertisements")
    public ResponseEntity<CommonResponse<FindAllMyAdvertisementResponseDto>> findMyAdvertisements(
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, advertisementService.findAllMyAdvertisement(loginMember));
    }

    @Operation(
            summary = "광고 수정",
            description = "로그인된 회원이 등록한 특정 광고를 수정한다.",
            tags = {"회원 API"}
    )
    @PutMapping("/my/my-advertisements/{advertisementId}")
    public ResponseEntity<CommonResponse<UpdateAdvertisementResponseDto>> updateAdvertisement(
            @RequestBody @Valid UpdateAdvertisementRequestDto requestDto,
            @PathVariable Long advertisementId,
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, advertisementService.updateAdvertisement(
                loginMember, advertisementId, requestDto));
    }

    @Operation(
            summary = "포인트 조회",
            description = "로그인된 회원의 총 포인트를 조회한다.",
            tags = {"회원 API"}
    )
    @GetMapping("/my/points")
    public ResponseEntity<CommonResponse<TotalPointsResponse>> getPoints(
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, pointService.getTotalPoints(loginMember.getId()));
    }

    @Operation(
            summary = "쿠폰 조회",
            description = "로그인된 회원의 모든 쿠폰을 조회한다.",
            tags = {"회원 API"}
    )
    @GetMapping("/my/coupons")
    public ResponseEntity<CommonResponse<CouponListResponse>> getCoupons(
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, couponService.getMyCoupons(loginMember.getId()));
    }
}