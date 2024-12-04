package com.example.elevendash.domain.store.dto.request;

import com.example.elevendash.global.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class RegisterStoreRequestDto {
    @NotBlank
    @Length(min = 2, max = 50)
    private final String storeName;

    private final String storeDescription;

    @NotBlank
    private final LocalTime openTime;

    @NotBlank
    private final LocalTime closeTime;

    @NotBlank
    @Length(min = 2, max = 50)
    private final String storeAddress;

    @NotBlank
    @ValidPhoneNumber
    private final String storePhone;

    @NotBlank
    private final Integer leastAmount;


}
