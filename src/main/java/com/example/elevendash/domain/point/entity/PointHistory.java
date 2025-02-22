package com.example.elevendash.domain.point.entity;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.point.enums.PointType;
import com.example.elevendash.global.entity.BaseCreatedTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point_history")
@Getter
@NoArgsConstructor
public class PointHistory extends BaseCreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PointType type;

    @Column(name = "description", nullable = false)
    private String description;

    @Builder
    public PointHistory(@NotNull Member member, @NotBlank Long orderId, @NotNull Integer amount, @NotNull Point point, @NotNull PointType type, @NotBlank String description) {
        this.member = member;
        this.orderId = orderId;
        this.amount = amount;
        this.point = point;
        this.type = type;
        this.description = description;
    }

}