package com.example.elevendash.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteBookmarkResponseDto {
    private final Long bookmarkId;
}
