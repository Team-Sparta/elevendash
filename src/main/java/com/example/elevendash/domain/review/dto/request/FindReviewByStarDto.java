package com.example.elevendash.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class FindReviewByStarDto {
    @Min(value = 1, message = "별점의 최하점은 1점입니다.")
    @Max(value = 5, message = "별점의 최고점은 5점입니다.")
    private int minStar;

    @Min(value = 1, message = "별점의 최하점은 1점입니다.")
    @Max(value = 5, message = "별점의 최고점은 5점입니다.")
    private int maxStar;

    public FindReviewByStarDto(int minStar, int maxStar) {
        this.minStar = minStar;
        this.maxStar = maxStar;
    }
}
