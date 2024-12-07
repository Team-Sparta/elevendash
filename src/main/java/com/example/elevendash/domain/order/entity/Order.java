package com.example.elevendash.domain.order.entity;


import com.example.elevendash.domain.coupon.entity.Coupon;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.order.enums.OrderStatus;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`orders`")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;

    @Column
    private String cancelMassage;


    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<OrderMenu> orderMenus;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "coupone_id", nullable = true)
    private Coupon coupon;

    public Order(Member member, Store store, Coupon coupon) {
        this.member = member;
        this.store = store;
        this.coupon = coupon;
    }



    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }


}
