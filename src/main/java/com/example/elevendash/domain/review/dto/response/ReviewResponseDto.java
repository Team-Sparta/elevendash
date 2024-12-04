package com.example.elevendash.domain.review.dto.response;

import com.example.elevendash.domain.review.entity.Comment;
import com.example.elevendash.domain.review.entity.Review;

import java.time.LocalDateTime;

public class ReviewResponseDto {
    Long reviewId;
    String storeName;
    String memberName;
    String content;
    int starRating;
    LocalDateTime createdAt;
    Comment comment;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.storeName = review.getStore().getStoreName();
        this.memberName = review.getMember().getName();
        this.content = review.getContent();
        this.starRating = review.getStarRating();
        this.createdAt = review.getCreatedAt();
    }
}
