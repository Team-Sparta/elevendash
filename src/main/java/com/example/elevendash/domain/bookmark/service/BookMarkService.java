package com.example.elevendash.domain.bookmark.service;

import com.example.elevendash.domain.bookmark.dto.response.AddBookmarkResponseDto;
import com.example.elevendash.domain.bookmark.dto.response.DeleteBookmarkResponseDto;
import com.example.elevendash.domain.bookmark.entity.Bookmark;
import com.example.elevendash.domain.bookmark.repository.BookMarkRepository;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookMarkService {
    private final BookMarkRepository bookMarkRepository;
    private final StoreRepository storeRepository;



    @Transactional
    public AddBookmarkResponseDto addBookmark(Member member, Long storeId){
        Store addBookmarkStore = storeRepository.findById(storeId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_STORE));
        Optional<Bookmark> checkBookmark = bookMarkRepository.findByStore_IdAndMember(storeId, member);
        if(checkBookmark.isPresent()){
            throw new BaseException(ErrorCode.ALREADY_BOOKMARK);
        }
        Bookmark bookmark = new Bookmark(member,addBookmarkStore);
        bookMarkRepository.save(bookmark);
        return new AddBookmarkResponseDto(bookmark.getId());
    }

    @Transactional
    public DeleteBookmarkResponseDto deleteBookmark(Member member, Long storeId, Long bookmarkId){
        Bookmark deleteBookmark = bookMarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOOKMARK));
        if(storeId.equals(deleteBookmark.getStore().getId()) && deleteBookmark.getMember().equals(member)){
            bookMarkRepository.delete(deleteBookmark);
        }
        else{
            throw new BaseException(ErrorCode.NOT_CORRECT_INFORMATION);
        }
        return new DeleteBookmarkResponseDto(deleteBookmark.getId());

    }
}
