package com.example.elevendash.domain.advertisement.controller;

import com.example.elevendash.domain.advertisement.dto.request.AddAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.request.RejectAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.*;
import com.example.elevendash.domain.advertisement.service.AdvertisementService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "광고 API",
        description = "광고 관련 API"
)
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
    @Operation(
            summary = "광고 생성",
            description = "광고 생성을 진행한다."
    )
    @PostMapping
    public ResponseEntity<CommonResponse<AddAdvertisementResponseDto>> addAdvertisement(
            @RequestBody @Valid AddAdvertisementRequestDto requestDto,
            @LoginMember @Parameter(hidden = true) Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,advertisementService.addAdvertisement(
                loginMember,requestDto
                )
        );
    }

    /**
     * 광고 삭제(stop) 하는 API
     * @param advertisementId
     * @param loginMember
     * @return
     */
    @Operation(
            summary = "광고 삭제",
            description = "광고 삭제를 진행한다."
    )
    @DeleteMapping("/{advertisementId}")
    public ResponseEntity<CommonResponse<DeleteAdvertisementResponseDto>> deleteAdvertisement(
            @PathVariable @Parameter(hidden = true) Long advertisementId,
            @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,advertisementService.deleteAdvertisement(loginMember,advertisementId));
    }

    /**
     * 광고를 거절하는 API
     * @param advertisementId 거절할 광고 ID
     * @param loginMember 로그인한 관리자 회원
     * @param requestDto 거절 사유 정보
     * @return 거절된 광고 정보
     */
    @Operation(
            summary = "광고 거절",
            description = "광고 거절을 진행한다."
    )
    @PutMapping("/{advertisementId}/reject")
    public ResponseEntity<CommonResponse<RejectAdvertisementResponseDto>> rejectAdvertisement(
            @PathVariable @Parameter(hidden = true) Long advertisementId,
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
    @Operation(
            summary = "광고 수락",
            description = "광고 수락을 진행한다."
    )
    @PutMapping("/{advertisementId}/accept")
    public ResponseEntity<CommonResponse<AcceptAdvertisementResponseDto>> acceptAdvertisement(
            @PathVariable @Parameter(hidden = true) Long advertisementId,
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
    @Operation(
            summary = "관리자 광고 조회",
            description = "관리자 광고 조회를 진행한다."
    )
    @GetMapping
    public ResponseEntity<CommonResponse<FindAllAdvertisementResponseDto>> findAllAdvertisement(
            @LoginMember @Parameter(hidden = true) Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS,advertisementService.findAllAdvertisement(loginMember));
    }


}
