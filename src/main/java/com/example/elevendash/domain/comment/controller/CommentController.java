package com.example.elevendash.domain.comment.controller;

import com.example.elevendash.domain.comment.dto.request.CommentRequestDto;
import com.example.elevendash.domain.comment.dto.response.CommentResponseDto;
import com.example.elevendash.domain.comment.service.CommentService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.review.dto.request.UpdateReviewDto;
import com.example.elevendash.global.annotation.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stores/{storeId}/reviews/{reviewId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @LoginMember Member loginMember,
            @Valid @RequestBody CommentRequestDto dto
    ){
        CommentResponseDto responseDto = commentService.create(loginMember, reviewId, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
