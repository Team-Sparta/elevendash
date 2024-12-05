package com.example.elevendash.domain.point.entity;

import com.example.elevendash.global.entity.BaseCreatedTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "points"
)
public class Point extends BaseCreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '포인트 고유 번호'")
    private Long id;

    @Column(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Long memberId;

    @Column(name = "order_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '주문 고유 번호'")
    private Long orderId;

    @Setter
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime expirationDate;


    @Builder
    public Point (Long memberId, Long orderId, Integer amount, LocalDateTime expirationDate) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.amount = amount;
        this.expirationDate = expirationDate;
    }
}
