package com.example.elevendash.domain.comment.service;

import com.example.elevendash.domain.comment.dto.request.CommentRequestDto;
import com.example.elevendash.domain.comment.dto.response.CommentResponseDto;
import com.example.elevendash.domain.comment.entity.Comment;
import com.example.elevendash.domain.comment.repository.CommentRepository;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.review.entity.Review;

import com.example.elevendash.domain.review.repository.ReviewRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public CommentResponseDto create(Member loginMember, Long reviewId, CommentRequestDto dto){

        validateOwner(loginMember);

        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        Comment comment = new Comment(dto, findReview);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment, findReview.getStore());
    }

    @Transactional
    public CommentResponseDto updateComment(Member loginMember, Long commentId, CommentRequestDto dto){

        validateOwner(loginMember);

        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        validateMember(loginMember, findComment);

        findComment.updateComment(dto);
        Comment savedComment = commentRepository.save(findComment);

        return new CommentResponseDto(savedComment);
    }

    public void delete(Member loginMember, Long commentId) {

        validateOwner(loginMember);

        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        validateMember(loginMember, findComment);

        commentRepository.delete(findComment);
    }

    private void validateOwner(Member loginMember){
        if(!loginMember.getRole().equals(MemberRole.OWNER)){
            throw new BaseException(ErrorCode.NOT_OWNER);
        }
    }

    private void validateMember(Member loginMember, Comment findComment){
        if (!findComment.getMember().getId().equals(loginMember.getId())) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_COMMNET);
        }
    }
}