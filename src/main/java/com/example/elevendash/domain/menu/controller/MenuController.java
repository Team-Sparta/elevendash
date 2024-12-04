package com.example.elevendash.domain.menu.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.dto.request.RegisterMenuRequestDto;
import com.example.elevendash.domain.menu.dto.response.RegisterMenuResponseDto;
import com.example.elevendash.domain.menu.service.MenuService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stores/")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    /**
     * 메뉴 등록 엔드포인트
     * @param requestDto
     * @param storeId
     * @param member
     * @return
     */
    @PostMapping("/{storeId}/menus/register")
    public ResponseEntity<CommonResponse<RegisterMenuResponseDto>> registerMenu(
            @RequestBody @Valid RegisterMenuRequestDto requestDto,
            @PathVariable Long storeId,
            @LoginMember Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,menuService.registerMenu(member, storeId, requestDto));
    }
}
