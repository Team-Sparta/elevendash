package com.example.elevendash.domain.review.dto.response;

import com.example.elevendash.domain.comment.dto.response.CommentResponseDto;
import com.example.elevendash.domain.comment.entity.Comment;
import com.example.elevendash.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PageReviewResponseDto {
    private final Long reviewId;
    private final String storeName;
    private final String memberName;
    private final String content;
    private final int starRating;
    private final LocalDateTime createdAt;
    private final CommentResponseDto comment;

    public PageReviewResponseDto(Review review, Comment comment) {
        this.reviewId = review.getId();
        this.storeName = review.getStore().getStoreName();
        this.memberName = review.getMember().getName();
        this.content = review.getContent();
        this.starRating = review.getStarRating();
        this.createdAt = review.getCreatedAt();
        this.comment = comment != null ? new CommentResponseDto(comment) : null;
    }
}
