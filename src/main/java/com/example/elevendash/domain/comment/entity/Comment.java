package com.example.elevendash.domain.comment.entity;

import com.example.elevendash.domain.comment.dto.request.CommentRequestDto;
import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public Comment(CommentRequestDto dto, Review review){
        this.content = dto.getContent();
        this.review = review;
    }

    public void updateComment(CommentRequestDto dto){
        this.content = dto.getContent();
    }
}
