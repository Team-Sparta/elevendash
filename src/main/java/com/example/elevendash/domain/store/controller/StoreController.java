package com.example.elevendash.domain.store.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.store.dto.request.RegisterStoreRequestDto;
import com.example.elevendash.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.elevendash.domain.store.dto.response.*;
import com.example.elevendash.domain.store.enums.SortMode;
import com.example.elevendash.domain.store.service.StoreService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "가게 API",
        description = "가게 관련 API"
)

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    /**
     * 상점 등록 엔드포인트
     *
     * @param requestDto
     * @param storeImage
     * @param loginMember
     * @return
     */
    @Operation(
            summary = "가게 등록",
            description = "가게 등록을 진행한다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<CommonResponse<RegisterStoreResponseDto>> registerStore(
            @LoginMember @Parameter(hidden = true) Member loginMember,
            @Valid @RequestPart(value = "request") RegisterStoreRequestDto requestDto,
            @RequestPart(name = "image", required = false) MultipartFile storeImage) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, storeService.registerStore(loginMember, storeImage, requestDto));
    }

    /**
     * 상점 삭제 엔드포인트
     *
     * @param storeId
     * @param loginMember
     * @return
     */
    @Operation(
            summary = "가게 삭제",
            description = "가게 삭제를 진행한다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{storeId}")
    ResponseEntity<CommonResponse<DeleteStoreResponseDto>> deleteStore(@PathVariable(name = "storeId") Long storeId,
                                                                       @LoginMember @Parameter(hidden = true) Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, storeService.deleteStore(loginMember, storeId));
    }

    /**
     * 상점 수정 엔드포인트
     *
     * @param storeId
     * @param loginMember
     * @return
     */
    @Operation(
            summary = "가게 수정",
            description = "가게 수정을 진행한다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{storeId}")
    ResponseEntity<CommonResponse<UpdateStoreResponseDto>> updateStore(@PathVariable(name = "storeId") Long storeId,
                                                                       @LoginMember @Parameter(hidden = true) Member loginMember,
                                                                       @RequestPart("request") @Valid UpdateStoreRequestDto requestDto,
                                                                       @RequestPart(name = "image", required = false) MultipartFile storeImage) {
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, storeService.updateStore(loginMember, storeId, storeImage, requestDto));
    }

    /**
     * 상점 단건 조회 엔드포인트
     *
     * @param storeId
     * @return
     */
    @Operation(
            summary = "가게 단건 조회",
            description = "가게 단건 조회를 진행한다."
    )
    @GetMapping("/{storeId}")
    ResponseEntity<CommonResponse<FindStoreResponseDto>> findStore(@PathVariable(name = "storeId") Long storeId) {
        return CommonResponse.success(SuccessCode.SUCCESS, storeService.findStore(storeId));
    }

    /**
     * 상점 목록 조회 엔드포인트
     *
     * @param loginMember 로그인한 사용자 정보
     * @param pageSize    페이지당 항목 수 (기본값: 10)
     * @param pageNumber  페이지 번호 (기본값: 1)
     * @param sortMode    정렬 방식
     * @param category    카테고리 필터 (기본값: CHICKEN)
     * @return 페이징된 상점 목록
     */
    @Operation(
            summary = "가게 다건 조회",
            description = "가게 다건 조회를 진행한다."
    )
    @GetMapping
    ResponseEntity<CommonResponse<FindAllStoreResponseDto>> findAllStore(
            @LoginMember @Parameter(hidden = true) Member loginMember,
            @RequestParam(defaultValue = "1") @Min(1) Integer pageSize,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageNumber,
            @RequestParam SortMode sortMode,
            @RequestParam(defaultValue = "CHICKEN") Categories category
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, storeService.findAllStore(loginMember, category, pageSize, pageNumber - 1, sortMode));
    }

}
