package com.example.elevendash.domain.point.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Amount {

    private Double originalAmount;

    @Setter
    private Double currentAmount;

    public Amount(Double originalAmount, Double currentAmount) {
        this.originalAmount = originalAmount;
        this.currentAmount = currentAmount;
    }
}