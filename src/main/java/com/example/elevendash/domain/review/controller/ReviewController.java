package com.example.elevendash.domain.review.controller;

import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.request.FindReviewByStarDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.domain.review.repository.ReviewRepository;
import com.example.elevendash.domain.review.service.ReviewService;
import jakarta.validation.Valid;
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
    private final ReviewRepository repository;

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
            @Valid @RequestBody FindReviewByStarDto dto,
            int page
    ){
        Page<ReviewResponseDto> reviews = service.findBystarRating(storeId, dto, page);
        return new ResponseEntity<>(reviews, HttpStatus.OK);

    }


}