package com.example.elevendash.domain.point.controller;

import com.example.elevendash.domain.member.dto.response.SignUpResponse;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.point.dto.response.TotalPointsResponse;
import com.example.elevendash.domain.point.service.PointService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping
    public ResponseEntity<CommonResponse<TotalPointsResponse>> getMyPoints(

            @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS, pointService.getTotalPoints(loginMember.getId()));
    }
}
