package com.example.elevendash.domain.order.entity;

import com.example.elevendash.domain.menu.entity.MenuOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "order_menu_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(1)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_menu_id")
    private OrderMenu orderMenu;

    @OneToOne
    @JoinColumn(name = "menu_option_id")
    private MenuOption menuOption;

    public OrderMenuOption(MenuOption menuOption, Long quantity, OrderMenu orderMenu) {
        this.menuOption = menuOption;
        this.quantity = quantity;
        this.orderMenu = orderMenu;
    }
}
