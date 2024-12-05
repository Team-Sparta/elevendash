package com.example.elevendash.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateReviewDto {

    @NotNull
    Long orderId;

    @NotNull
    String content;

    @NotNull
    @Min(value = 1, message = "별점의 최하점은 1점입니다.")
    @Max(value = 5, message = "별점의 최고점은 5점입니다.")
    int starRating;

}
