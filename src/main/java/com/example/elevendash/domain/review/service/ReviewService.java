package com.example.elevendash.domain.review.service;

import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.request.FindReviewByStarDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.entity.Review;

import com.example.elevendash.domain.review.repository.CommentRepository;
import com.example.elevendash.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    public ReviewResponseDto create(Long storeId, CreateReviewDto dto){
//        Order findOrder = orderRepository.findByIdOrElseThrow(dto.getOrderId());
//        if(!findOrder.getOrderStatus().equals("배달완료")){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "배달이 완료된 후에 리뷰를 작성하실 수 있습니다.");
//        }
//        Review review = new Review(dto);
//        Review savedReview = reviewRepository.save(review);
//
//        return new ReviewResponseDto(savedReview);
        return null;
    }


    public Page<ReviewResponseDto> find(Long storeId, Long memberId, int page){


        Pageable pageable = PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewResponseDto> reviews = reviewRepository.findByStoreIdPage(storeId, memberId, pageable).map(review -> new ReviewResponseDto(review, review.getComment()));

        return reviews;
    }

    public Page<ReviewResponseDto> findBystarRating(Long storeId, FindReviewByStarDto dto, int page){

        Pageable pageable = PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewResponseDto> reviews = reviewRepository.findByStarRating(storeId, dto.getMinStar(), dto.getMaxStar(), pageable).map(review -> new ReviewResponseDto(review, review.getComment()));

        return reviews;
    }

}