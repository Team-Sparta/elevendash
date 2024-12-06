package com.example.elevendash.domain.review.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.request.UpdateReviewDto;
import com.example.elevendash.domain.review.dto.response.PageReviewResponseDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.service.ReviewService;
import com.example.elevendash.global.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "리뷰 API",
        description = "리뷰 관련 API"
)
@RestController
@RequestMapping("/stores/{storeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 작성",
            description = "리뷰 작성을 진행한다.",
            tags = {"리뷰 API"}
    )
    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(
            @PathVariable Long storeId,
            @LoginMember Member loginMember,
            @Valid @RequestBody CreateReviewDto dto
    ){
        ReviewResponseDto responseDto = reviewService.create(storeId, loginMember.getId(), dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "리뷰 조회",
            description = "리뷰 조회를 진행한다.(리뷰 조회 시 댓글이 함께 조회됨)",
            tags = {"리뷰 API"}
    )
    @GetMapping
    public ResponseEntity<Page<PageReviewResponseDto>> find(
            @PathVariable Long storeId,
            @LoginMember Member loginMember,
            @PageableDefault(size = 2, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<PageReviewResponseDto> reviews = reviewService.find(storeId, loginMember.getId(), pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @Operation(
            summary = "리뷰 별점 조회",
            description = "리뷰의 별점 범위를 지정해서 조회를 진행한다.",
            tags = {"리뷰 API"}
    )
    @GetMapping("/star")
    public ResponseEntity<Page<PageReviewResponseDto>> findByStar(
            @PathVariable Long storeId,
            @RequestParam
            @Min(value = 1, message = "별점의 최하점은 1점입니다.")
            @Max(value = 5, message = "별점의 최고점은 5점입니다.")
            int minStar,
            @RequestParam
            @Min(value = 1, message = "별점의 최하점은 1점입니다.")
            @Max(value = 5, message = "별점의 최고점은 5점입니다.")
            int maxStar,
            @PageableDefault(size = 2, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){

        Page<PageReviewResponseDto> reviews = reviewService.findBystarRating(storeId, minStar, maxStar, pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @Operation(
            summary = "리뷰 수정",
            description = "리뷰 수정을 진행한다.",
            tags = {"리뷰 API"}
    )
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @LoginMember Member loginmember,
            @Valid @RequestBody UpdateReviewDto dto
    ){
        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, loginmember.getId(), dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰 삭제를 진행한다.",
            tags = {"리뷰 API"}
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @LoginMember Member loginmember
    ){
        reviewService.delete(reviewId, loginmember.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}