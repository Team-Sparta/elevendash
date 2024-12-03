package com.example.elevendash.domain.order.entity;


import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.Store;

@Entity
@Getter
@NoArgsConstructor
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private String manuName;

    private String cart;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Order(Long orderId, String orderStatus, String manuName, String cart) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.manuName = manuName;
        this.cart = cart;
    }

    public void updateStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
