package com.example.elevendash.domain.comment.controller;

import com.example.elevendash.domain.comment.dto.request.CommentRequestDto;
import com.example.elevendash.domain.comment.dto.response.CommentResponseDto;
import com.example.elevendash.domain.comment.service.CommentService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.review.dto.request.UpdateReviewDto;
import com.example.elevendash.global.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "댓글 API",
        description = "댓글 관련 API"
)
@RestController
@RequestMapping("/stores/{storeId}/reviews/{reviewId}/comments")
@RequiredArgsConstructor
@PreAuthorize(value = "hasRole('OWNER')")
public class CommentController {
    private final CommentService commentService;

    @Operation(
            summary = "댓글 작성",
            description = "댓글 작성을 진행한다.",
            tags = {"댓글 API"}
    )
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

    @Operation(
            summary = "댓글 수정",
            description = "댓글 수정을 진행한다.",
            tags = {"댓글 API"}
    )
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @LoginMember Member loginMember,
            @Valid @RequestBody CommentRequestDto dto
    ){
        CommentResponseDto responseDto = commentService.updateComment(loginMember, commentId, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "댓글 삭제",
            description = "댓글 삭제를 진행한다.",
            tags = {"댓글 API"}
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @LoginMember Member loginMember
    ){
        commentService.delete(loginMember, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
