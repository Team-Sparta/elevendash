package com.example.elevendash.domain.bookmark.controller;

import com.example.elevendash.domain.bookmark.dto.response.AddBookmarkResponseDto;
import com.example.elevendash.domain.bookmark.dto.response.DeleteBookmarkResponseDto;
import com.example.elevendash.domain.bookmark.service.BookMarkService;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class BookmarkController {
    private final BookMarkService bookMarkService;

    @PostMapping("/stores/{storeId}/bookmarks")
    public ResponseEntity<CommonResponse<AddBookmarkResponseDto>> addBookmark(
            @PathVariable("storeId") Long storeId,
            @LoginMember Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT,bookMarkService.addBookmark(loginMember,storeId));
    }

    @DeleteMapping("/stores/{storeId}/bookmarks/{bookmarkId}")
    public ResponseEntity<CommonResponse<DeleteBookmarkResponseDto>> deleteBookmark(
            @PathVariable("bookmarkId") Long bookmarkId,
            @PathVariable("storeId") Long storeId,
            @LoginMember Member loginMember
    ){
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE,bookMarkService.deleteBookmark(loginMember,storeId,bookmarkId));
    }

}
