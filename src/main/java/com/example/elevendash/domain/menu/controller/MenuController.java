package com.example.elevendash.domain.menu.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.dto.request.RegisterMenuRequestDto;
import com.example.elevendash.domain.menu.dto.response.DeleteMenuResponseDto;
import com.example.elevendash.domain.menu.dto.response.RegisterMenuResponseDto;
import com.example.elevendash.domain.menu.service.MenuService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("stores/")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    /**
     * 메뉴 등록 API
     *
     * @param requestDto 메뉴 등록 정보 (이름, 가격, 이미지, 카테고리)
     * @param storeId 가게 식별자
     * @param member 로그인한 사용자 정보
     * @return 등록된 메뉴 정보
     * @throws  가게를 찾을 수 없는 경우
     * @throws  권한이 없는 경우
     */
    @PostMapping("/{storeId}/menus/register")
    public ResponseEntity<CommonResponse<RegisterMenuResponseDto>> registerMenu(
            @RequestBody @Valid RegisterMenuRequestDto requestDto,
            @PathVariable Long storeId,
            @RequestParam("menuImage") MultipartFile menuImage,
            @LoginMember Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,menuService.registerMenu(member, menuImage,storeId, requestDto));
    }

    /**
     * 메뉴 삭제 API
     * @param storeId
     * @param menuId
     * @param member
     * @return
     */
    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<CommonResponse<DeleteMenuResponseDto>> deleteMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @LoginMember Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,menuService.deleteMenu(member,storeId,menuId));
    }
}
