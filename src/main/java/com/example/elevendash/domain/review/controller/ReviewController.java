package com.example.elevendash.domain.review.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.request.UpdateReviewDto;
import com.example.elevendash.domain.review.dto.response.PageReviewResponseDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.service.ReviewService;
import com.example.elevendash.global.annotation.LoginMember;
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


@RestController
@RequestMapping("/stores/{storeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(
            @PathVariable Long storeId,
            @Valid @RequestBody CreateReviewDto dto
    ){
        ReviewResponseDto responseDto = reviewService.create(storeId, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PageReviewResponseDto>> find(
            @PathVariable Long storeId,
            @LoginMember Member loginMember,
            int page
    ){
        Page<PageReviewResponseDto> reviews = reviewService.find(storeId, loginMember, page);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

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
            int page
    ){

        Page<PageReviewResponseDto> reviews = reviewService.findBystarRating(storeId, minStar, maxStar, page);
        return new ResponseEntity<>(reviews, HttpStatus.OK);

    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @LoginMember Member loginmember,
            @Valid @RequestBody UpdateReviewDto dto
    ){
        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, loginmember.getId(), dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}