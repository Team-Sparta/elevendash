package com.example.elevendash.domain.review.service;

import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.response.ReviewResponseDto;
import com.example.elevendash.domain.review.entity.Review;

import com.example.elevendash.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public ReviewResponseDto create(CreateReviewDto dto){
//        Order findorder = orderRepository.findByIdOrElseThrow(dto.getOrderId());
//        if(!findorder.getOrderStatus().equals("배달완료")){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "배달이 완료된 후에 리뷰를 작성하실 수 있습니다.");
//        }
//        Review review = new Review(dto);
//        Review savedReview = reviewRepository.save(review);
//
//        return new ReviewResponseDto(savedReview);
        return null;
    }

}