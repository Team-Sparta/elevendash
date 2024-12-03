package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    OrderRepository orderRepository;
}
