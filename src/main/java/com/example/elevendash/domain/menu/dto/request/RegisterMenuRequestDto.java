package com.example.elevendash.domain.menu.dto.request;

import com.example.elevendash.domain.menu.enums.Categories;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class RegisterMenuRequestDto {

    @NotBlank
    @Length(max = 50)
    private final String menuName;

    @NotNull
    @Min(0)
    private final Integer menuPrice;

    @Lob
    @NotBlank
    private final String menuImage;

    @NotNull
    private final Categories menuCategory;
}
