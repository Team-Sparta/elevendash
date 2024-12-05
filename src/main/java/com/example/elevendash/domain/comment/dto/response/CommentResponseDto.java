package com.example.elevendash.domain.comment.dto.response;

import com.example.elevendash.domain.comment.entity.Comment;
import com.example.elevendash.domain.store.entity.Store;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final String storeName;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentResponseDto(Comment comment, Store store) {
        this.commentId = comment.getId();
        this.storeName = store.getStoreName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.storeName = comment.getReview().getStore().getStoreName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}