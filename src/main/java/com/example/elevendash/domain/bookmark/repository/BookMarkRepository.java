package com.example.elevendash.domain.bookmark.repository;

import com.example.elevendash.domain.bookmark.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
}
