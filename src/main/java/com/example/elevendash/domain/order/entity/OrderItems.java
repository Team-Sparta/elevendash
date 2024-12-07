package com.example.elevendash.domain.order.entity;

import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_menu")
public class OrderItems extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;

    private Long quantity;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    public OrderItems (String menuName, Long quantity, Long price) {
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
    }
}
