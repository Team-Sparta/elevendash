package com.example.elevendash.domain.bookmark.repository;

import com.example.elevendash.domain.bookmark.entity.Bookmark;
import com.example.elevendash.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByStore_IdAndMember(Long storeId, Member member);
}
