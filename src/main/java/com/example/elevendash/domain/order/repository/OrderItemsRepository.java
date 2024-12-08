package com.example.elevendash.domain.order.repository;

import com.example.elevendash.domain.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderMenu, Long> {
}
