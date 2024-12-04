package com.example.elevendash.domain.store.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.dto.request.RegisterStoreRequestDto;
import com.example.elevendash.domain.store.dto.response.DeleteStoreResponseDto;
import com.example.elevendash.domain.store.dto.response.RegisterStoreResponseDto;
import com.example.elevendash.domain.store.service.StoreService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    /**
     * 상점 등록 엔드포인트
     * @param requestDto
     * @param storeImage
     * @param loginMember
     * @return
     */
    @PostMapping("/register")
    ResponseEntity<CommonResponse<RegisterStoreResponseDto>> registerStore(@RequestBody @Valid RegisterStoreRequestDto requestDto,
                                                                     @RequestParam("image") MultipartFile storeImage,
                                                                     @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,storeService.registerStore(loginMember,storeImage ,requestDto));
    }

    /**
     * 상점 삭제 엔드포인트
     * @param storeId
     * @param loginMember
     * @return
     */
    @DeleteMapping("/{storeId}")
    ResponseEntity<CommonResponse<DeleteStoreResponseDto>> deleteStore(@PathVariable(name = "storeId") Long storeId,
                                                                       @LoginMember Member loginMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,storeService.deleteStore(loginMember,storeId));
    }

}
