package com.example.elevendash.domain.store.dto.request;

import com.example.elevendash.global.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class RegisterStoreRequestDto {
    @NotBlank(message = "음식점 이름은 필수입니다")
    @Length(min = 2, max = 50,message = "음식점이름은 2~50글자 입니다")
    private final String storeName;

    private final String storeDescription;

    @NotNull(message = "시작 시간은 필수입니다")
    private final LocalTime openTime;

    @NotNull(message = "마감 시간은 필수입니다")
    private final LocalTime closeTime;

    @NotBlank(message = "주소는 필수입니다")
    @Length(min = 2, max = 50, message = "주소는 2~50글자 입니다")
    private final String storeAddress;

    @NotBlank(message = "연락처는 필수입니다")
    @ValidPhoneNumber
    private final String storePhone;

    @NotNull(message = "최소 주문금액은 필수입니다")
    @Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다")
    private final Integer leastAmount;

    private final MultipartFile multipartFile;


}
