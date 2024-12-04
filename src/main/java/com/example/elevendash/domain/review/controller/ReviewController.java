package com.example.elevendash.domain.review.controller;

import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    ) {
        ReviewResponseDto responseDto = service.create(storeId, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}