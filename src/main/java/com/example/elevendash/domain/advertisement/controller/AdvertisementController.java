package com.example.elevendash.domain.advertisement.controller;

import com.example.elevendash.domain.advertisement.dto.request.AddAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.AddAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.service.AdvertisementService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
