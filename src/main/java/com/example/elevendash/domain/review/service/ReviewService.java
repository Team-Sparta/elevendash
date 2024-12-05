package com.example.elevendash.domain.review.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;

import com.example.elevendash.domain.review.repository.ReviewRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public ReviewResponseDto create(Long storeId, CreateReviewDto dto){
//        Order findOrder = orderRepository.findByIdOrElseThrow(dto.getOrderId());
//        if(!findOrder.getOrderStatus().equals("배달완료")){
//            throw new BaseException(ErrorCode.NOT_DELIVERED);
//        }
//        Review review = new Review(dto);
//        Review savedReview = reviewRepository.save(review);
//
//        return new ReviewResponseDto(savedReview);
        return null;
    }

    private final int DEFAULT_PAGE_SIZE = 2;

    private Pageable pageable(int page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Page<ReviewResponseDto> find(Long storeId, Member member, int page){

        Pageable pageable = pageable(page);
        Page<ReviewResponseDto> reviews = reviewRepository.findByStoreIdPage(storeId, member.getId(), pageable).map(review -> new ReviewResponseDto(review, review.getComment()));

        return reviews;
    }

    public Page<ReviewResponseDto> findBystarRating(Long storeId, int minStar, int maxStar, int page){

        if(minStar > maxStar) {
            throw new BaseException(ErrorCode.BAD_STARRATING);
        }
        Pageable pageable = pageable(page);
        Page<ReviewResponseDto> reviews = reviewRepository.findByStarRating(storeId, minStar, maxStar, pageable).map(review -> new ReviewResponseDto(review, review.getComment()));

        return reviews;
    }

}