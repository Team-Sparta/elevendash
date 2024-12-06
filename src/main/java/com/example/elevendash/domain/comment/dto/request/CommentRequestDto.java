package com.example.elevendash.domain.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotNull
    String content;

}
