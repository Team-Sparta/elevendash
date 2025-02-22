package com.example.elevendash.domain.menu.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.dto.request.AddMenuOptionRequestDto;
import com.example.elevendash.domain.menu.dto.request.RegisterMenuRequestDto;
import com.example.elevendash.domain.menu.dto.request.UpdateMenuRequestDto;
import com.example.elevendash.domain.menu.dto.response.*;
import com.example.elevendash.domain.menu.service.MenuService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Tag(
        name = "메뉴 API",
        description = "메뉴 관련 API"
)
@RestController
@RequestMapping("/stores")
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

     */
    @Operation(
            summary = "메뉴 등록",
            description = "메뉴 등록을 진행한다."
    )
    @PostMapping("/{storeId}/menus/register")
    public ResponseEntity<CommonResponse<RegisterMenuResponseDto>> registerMenu(
            @RequestPart("request") @Valid RegisterMenuRequestDto requestDto,
            @PathVariable("storeId") Long storeId,
            @RequestPart(name = "image", required = false) @NotNull MultipartFile menuImage,
            @LoginMember @Parameter(hidden = true) Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,menuService.registerMenu(member, menuImage,storeId, requestDto));
    }

    /**
     * 메뉴 삭제 API
     * @param storeId
     * @param menuId
     * @param member
     * @return
     */
    @Operation(
            summary = "메뉴 삭제",
            description = "메뉴 삭제를 진행한다."
    )
    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<CommonResponse<DeleteMenuResponseDto>> deleteMenu(
            @PathVariable("storeId") Long storeId,
            @PathVariable("menuId") Long menuId,
            @LoginMember @Parameter(hidden = true) Member member) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,menuService.deleteMenu(member,storeId,menuId));
    }

    /**
     * 메뉴 수정 API
     * @param storeId
     * @param menuId
     * @param member
     * @param requestDto
     * @return
     */
    @Operation(
            summary = "메뉴 수정",
            description = "메뉴 수정을 진행한다."
    )
    @PutMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<CommonResponse<UpdateMenuResponseDto>> updateMenu(
            @PathVariable("storeId") Long storeId,
            @PathVariable("menuId") Long menuId,
            @LoginMember @Parameter(hidden = true) Member member,
            @RequestPart(name = "image", required = false) @NotNull MultipartFile menuImage,
            @RequestPart("request") @Valid UpdateMenuRequestDto requestDto) {
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE,menuService.updateMenu(member,menuImage,storeId,menuId,requestDto));
    }

    /**
     * 메뉴 옵션 추가 API
     * @param storeId
     * @param menuId
     * @param member
     * @param requestDto
     * @return
     */
    @Operation(
            summary = "메뉴 옵션 등록",
            description = "메뉴 옵션 등록을 진행한다."
    )
    @PostMapping("/{storeId}/menus/{menuId}/menu-options/add")
    public ResponseEntity<CommonResponse<AddMenuOptionResponseDto>> addMenuOption(
            @PathVariable("storeId") Long storeId,
            @PathVariable("menuId") Long menuId,
            @LoginMember @Parameter(hidden = true) Member member,
            @RequestBody @Valid AddMenuOptionRequestDto requestDto) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,menuService.addOption(member,storeId,menuId,requestDto));
    }

    /**
     * 메누 삭제 API
     * @param storeId
     * @param menuId
     * @param member
     * @param menuOptionId
     * @return
     */
    @Operation(
            summary = "메뉴 옵션 삭제",
            description = "메뉴 옵션 삭제를 진행한다."
    )
    @DeleteMapping("/{storeId}/menus/{menuId}/menu-options/{menuOptionId}")
    public ResponseEntity<CommonResponse<DeleteMenuOptionResponseDto>> deleteMenuOption(
            @PathVariable("storeId") Long storeId,
            @PathVariable("menuId") Long menuId,
            @LoginMember @Parameter(hidden = true) Member member,
            @PathVariable Long menuOptionId) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,menuService.deleteOption(member,storeId,menuId,menuOptionId));
    }

    /**
     * 메뉴 단건 조회 API
     * @param storeId
     * @param menuId
     * @return
     */
    @Operation(
            summary = "메뉴 단건 조회",
            description = "메뉴 단건 조회를 진행한다."
    )
    @GetMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<CommonResponse<FindMenuResponseDto>> findMenu(
            @PathVariable("storeId") Long storeId,
            @PathVariable("menuId") Long menuId){
        return CommonResponse.success(SuccessCode.SUCCESS,menuService.findMenu(storeId,menuId));
    }


}
