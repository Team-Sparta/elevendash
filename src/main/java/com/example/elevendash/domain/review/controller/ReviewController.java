package com.example.elevendash.domain.review.controller;

import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stores/{storeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(
            @PathVariable Long storeId,
            @Valid @RequestBody CreateReviewDto dto
    ){
        ReviewResponseDto responseDto = service.create(storeId, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> find(
            @PathVariable Long storeId,
            Long memberId,
            int page
    ){
        Page<ReviewResponseDto> reviews = service.find(storeId, memberId, page);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/star")
    public ResponseEntity<Page<ReviewResponseDto>> findBystar(
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

        Page<ReviewResponseDto> reviews = service.findBystarRating(storeId, minStar, maxStar, page);
        return new ResponseEntity<>(reviews, HttpStatus.OK);

    }


}