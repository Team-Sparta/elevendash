package com.example.elevendash.domain.bookmark.controller;

import com.example.elevendash.domain.bookmark.dto.response.AddBookmarkResponseDto;
import com.example.elevendash.domain.bookmark.dto.response.DeleteBookmarkResponseDto;
import com.example.elevendash.domain.bookmark.service.BookMarkService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "즐겨찾기 API",
        description = "즐겨찾기 관련 API"
)
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class BookmarkController {
    private final BookMarkService bookMarkService;

    /**
     * 북마크 생성 API
     * @param storeId
     * @param loginMember
     * @return
     */
    @Operation(
            summary = "북마크 생성",
            description = "북마크 생성을 진행한다."
    )
    @PostMapping("/stores/{storeId}/bookmarks")
    public ResponseEntity<CommonResponse<AddBookmarkResponseDto>> addBookmark(
            @PathVariable("storeId") Long storeId,
            @LoginMember @Parameter(hidden = true) Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,bookMarkService.addBookmark(loginMember,storeId));
    }

    /**
     * 북마크 삭제 API
     * @param bookmarkId
     * @param storeId
     * @param loginMember
     * @return
     */
    @Operation(
            summary = "북마크 삭제",
            description = "북마크 삭제를 진행한다."
    )
    @DeleteMapping("/stores/{storeId}/bookmarks/{bookmarkId}")
    public ResponseEntity<CommonResponse<DeleteBookmarkResponseDto>> deleteBookmark(
            @PathVariable("bookmarkId") Long bookmarkId,
            @PathVariable("storeId") Long storeId,
            @LoginMember @Parameter(hidden = true) Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,bookMarkService.deleteBookmark(loginMember,storeId,bookmarkId));
    }

}
