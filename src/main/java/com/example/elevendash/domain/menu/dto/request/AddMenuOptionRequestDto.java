package com.example.elevendash.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
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
    // 옵션 적용 여부
}
