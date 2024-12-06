package com.example.elevendash.domain.order.entity;


import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private String orderStatus;

    private String cancelMassage;

    @Column(nullable = false)
    @OneToMany()
    @JoinColumn(name = "id")
    private List<Menu> menu = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Order(Long id,Long price, String orderStatus, List<Menu> manus) {
        this.id = id;
        this.price = price;
        this.orderStatus = orderStatus;
        this.menu = manus;
    }

    public void updateStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void CancelMassage (String cancelMassage) {
        this.cancelMassage = cancelMassage;
    }
}
