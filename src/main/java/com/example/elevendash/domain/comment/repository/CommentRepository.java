package com.example.elevendash.domain.comment.repository;

import com.example.elevendash.domain.comment.entity.Comment;
import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
    }

    @Modifying
    @Transactional
    @Query("delete from Comment where id = :commentId")
    void deleteCommentByCommentId(@Param("commentId") Long commentId);

}
