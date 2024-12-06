package com.example.elevendash.domain.dashboard.controller;

import com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse;
import com.example.elevendash.domain.dashboard.enums.PeriodType;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.order.service.OrderService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(
        name = "관리자 대시보드 API",
        description = "관리자 대시보드 통계 관련 API"
)
@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashBoardController {

    private final OrderService orderService;

    @Operation(
            summary = "통계 조회",
            description = "관리자 대시보드를 위한 통계 데이터를 조회합니다. ADMIN 권한이 필요합니다."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statistics")
    public ResponseEntity<CommonResponse<StatisticsResponse>> getStatistics(
            @Parameter(description = "통계 조회 기간 타입 (DAILY, MONTHLY, ANNUAL). 기본값은 MONTHLY입니다.", example = "MONTHLY")
            @RequestParam(required = false, defaultValue = "MONTHLY") PeriodType periodType,

            @Parameter(description = "통계 조회 시작 날짜 (ISO 형식 yyyy-MM-dd).", example = "2023-01-01")
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "통계 조회 종료 날짜 (ISO 형식 yyyy-MM-dd).", example = "2023-01-31")
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "통계를 조회할 특정 매장 ID.")
            @RequestParam(required = false) Long storeId,

            @Parameter(description = "통계 조회를 위한 카테고리 필터.", example = "FOOD")
            @RequestParam(required = false) Categories categories,

            @Parameter(hidden = true)
            @LoginMember Member loginMember
    ) {
        StatisticsResponse statistics = orderService.getStatistics(periodType, startDate, endDate, storeId, categories);
        return CommonResponse.success(SuccessCode.SUCCESS, statistics);
    }
}