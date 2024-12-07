package com.example.elevendash.domain.order.entity;


import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long price;

    @ColumnDefault("주문 완료")
    private String orderStatus;

    @ColumnDefault("")
    private String cancelMassage;


    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Order(Long price, Member member, Store store) {
        this.price = price;
        this.member = member;
        this.store = store;
    }



    public void updateStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }


}
