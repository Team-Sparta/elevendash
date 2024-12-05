package com.example.elevendash.domain.point.entity;

import com.example.elevendash.domain.point.enums.PointType;
import com.example.elevendash.global.entity.BaseCreatedTimeEntity;
import jakarta.persistence.*;
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

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Setter
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type;

    private String description;

    @Builder
    public PointHistory(Long memberId, Integer amount, Point point, PointType type, String description) {
        this.memberId = memberId;
        this.amount = amount;
        this.point = point;
        this.type = type;
        this.description = description;
    }

}