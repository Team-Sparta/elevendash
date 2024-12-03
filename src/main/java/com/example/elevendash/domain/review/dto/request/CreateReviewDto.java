package com.example.elevendash.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateReviewDto {

    @NotNull
    Long orderId;
    @NotNull
    String content;
    @NotNull
    int starRating;

}
