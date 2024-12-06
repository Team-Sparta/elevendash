package com.example.elevendash.domain.review.service;

import com.example.elevendash.domain.comment.entity.Comment;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.request.UpdateReviewDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.dto.response.PageReviewResponseDto;
import com.example.elevendash.domain.review.entity.Review;

import com.example.elevendash.domain.review.repository.ReviewRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public ReviewResponseDto create(Long storeId, Long longinMemberId, CreateReviewDto dto){
        Order findOrder = orderRepository.findByIdOrElseThrow(dto.getOrderId());

        if(!findOrder.getMember().getId().equals(longinMemberId)){
            throw new BaseException(ErrorCode.UNAUTHORIZED_REVIEW);
        }

        if(!findOrder.getOrderStatus().equals("배달완료")){
            throw new BaseException(ErrorCode.NOT_DELIVERED);
        }

        Review review = new Review(dto, findOrder);
        Review savedReview = reviewRepository.save(review);

        return new ReviewResponseDto(savedReview);
    }

    public Page<PageReviewResponseDto> find(Long storeId, Long memberId, Pageable pageable){

        Page<PageReviewResponseDto> reviews = reviewRepository.findByStore_IdAndMemberNotPage(storeId, memberId, pageable)
                .map(review -> new PageReviewResponseDto(review, review.getComment()));

        return reviews;
    }

    public Page<PageReviewResponseDto> findBystarRating(Long storeId, int minStar, int maxStar, Pageable pageable){

        if(minStar > maxStar) {
            throw new BaseException(ErrorCode.BAD_STARRATING);
        }
        Page<PageReviewResponseDto> reviews = reviewRepository.findByStarRating(storeId, minStar, maxStar, pageable)
                .map(review -> new PageReviewResponseDto(review, review.getComment()));

        return reviews;
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, Long loginMemberId, UpdateReviewDto dto){
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        validateMember(loginMemberId, findReview);

        findReview.updateReview(dto);
        Review savedReview = reviewRepository.save(findReview);

        return new ReviewResponseDto(savedReview);
    }

    @Transactional
    public void delete(Long reviewId, Long loginMemberId) {
        Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

        validateMember(loginMemberId, findReview);

        reviewRepository.delete(findReview);
    }

    private void validateMember(Long loginMemberId, Review findReview){
        if (!findReview.getMember().getId().equals(loginMemberId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_REVIEW);
        }
    }

}