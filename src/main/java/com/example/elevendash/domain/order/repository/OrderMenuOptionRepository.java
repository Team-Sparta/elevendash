package com.example.elevendash.domain.order.repository;

import com.example.elevendash.domain.order.entity.OrderMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface OrderMenuOptionRepository extends JpaRepository<OrderMenuOption, Long> {
}
