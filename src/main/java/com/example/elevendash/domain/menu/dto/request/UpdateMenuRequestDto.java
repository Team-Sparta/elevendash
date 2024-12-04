package com.example.elevendash.domain.menu.dto.request;

import com.example.elevendash.domain.menu.enums.Categories;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class UpdateMenuRequestDto {
    @NotBlank
    @Length(max = 50)
    private String menuName;

    @NotNull
    @Min(0)
    private Integer menuPrice;

    @NotNull
    private Categories menuCategory;
}
