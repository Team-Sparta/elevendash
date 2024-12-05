package com.example.elevendash.domain.advertisement.controller;

import com.example.elevendash.domain.advertisement.dto.request.AddAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.request.RejectAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.*;
import com.example.elevendash.domain.advertisement.service.AdvertisementService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    /**
     * 광고를 신청하는 API
     * @param requestDto
     * @param loginMember
     * @return
     */
    @PostMapping
    public ResponseEntity<CommonResponse<AddAdvertisementResponseDto>> addAdvertisement(
            @RequestBody @Valid AddAdvertisementRequestDto requestDto,
            @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,advertisementService.addAdvertisement(
                loginMember,requestDto
                )
        );
    }

    @DeleteMapping("/{advertisementId}")
    public ResponseEntity<CommonResponse<DeleteAdvertisementResponseDto>> deleteAdvertisement(
            @PathVariable Long advertisementId,
            @LoginMember Member loginMember,
            @RequestParam Long storeId) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,advertisementService.deleteAdvertisement(loginMember,storeId,advertisementId));
    }

    /**
     * 광고를 거절하는 API
     * @param advertisementId 거절할 광고 ID
     * @param loginMember 로그인한 관리자 회원
     * @param requestDto 거절 사유 정보
     * @return 거절된 광고 정보
     */
    @PutMapping("/{advertisementId}/reject")
    public ResponseEntity<CommonResponse<RejectAdvertisementResponseDto>> rejectAdvertisement(
            @PathVariable Long advertisementId,
            @LoginMember Member loginMember,
            @RequestBody @Valid RejectAdvertisementRequestDto requestDto){
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE,advertisementService.rejectAdvertisement(
                loginMember,
                advertisementId,
                requestDto)
        );
    }

    /**
     * 굉고 수락하는 API
     * @param advertisementId
     * @param loginMember
     * @return
     */
    @PutMapping("/{advertisementId}/accept")
    public ResponseEntity<CommonResponse<AcceptAdvertisementResponseDto>> acceptAdvertisement(
            @PathVariable Long advertisementId,
            @LoginMember Member loginMember
            ){
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE,advertisementService.acceptAdvertisement(
                loginMember,
                advertisementId
                )
        );
    }

    /**
     * 관리자 광고 조회 기능 API
     * @param loginMember
     * @return
     */
    @GetMapping
    public ResponseEntity<CommonResponse<FindAllAdvertisementResponseDto>> findAllAdvertisement(
            @LoginMember Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS,advertisementService.findAllAdvertisement(loginMember));
    }
}
