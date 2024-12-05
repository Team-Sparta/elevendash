package com.example.elevendash.domain.review.entity;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.review.dto.request.CreateReviewDto;
import com.example.elevendash.domain.review.dto.request.UpdateReviewDto;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="reviews")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "star_rating", nullable = false)
    private int starRating;

    @OneToOne
    @JoinColumn(name="order_id", unique = true)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Review(CreateReviewDto dto){
        this.content = dto.getContent();
        this.starRating = dto.getStarRating();
    }

    public void updateReview(UpdateReviewDto dto) {
        this.content = dto.getContent();
        this.starRating = dto.getStarRating();
    }


}
