package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService (OrderRepository orderService) {
        this.orderRepository = orderService;
    }

    public Order findOrderById (Long ordersId) {
        return orderRepository.findById(ordersId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
    }

    public OrderCheckResponseDto orderDetails(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getOrderStatus().equals("주문 거절")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다");
        }
        return OrderCheckResponseDto.toDto(findOrderById(orderId));
    }

}
