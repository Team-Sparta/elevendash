package com.example.elevendash.domain.review.repository;

import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r left join fetch r.comment where r.store.id = :storeId and r.member.id != :memberId")
    Page<Review> findByStore_IdAndMemberNotPage(Long storeId, Long memberId, Pageable pageable);

    @Query("select r from Review r where r.store.id=:storeId and r.starRating between :minStar and :maxStar")
    Page<Review> findByStarRating(Long storeId, int minStar, int maxStar, Pageable pageable);

    default Review findByIdOrElseThrow(Long reviewId) {
        return findById(reviewId).orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_REVIEW));
    }
}
