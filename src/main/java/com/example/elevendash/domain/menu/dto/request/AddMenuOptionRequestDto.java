package com.example.elevendash.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Getter
@RequiredArgsConstructor
public class AddMenuOptionRequestDto {
    // 옵션 내용
    @NotBlank
    @Length(min = 1, max = 50)
    private final String content;
    @NotNull
    @Min(0)
    private final Integer optionPrice;
}
