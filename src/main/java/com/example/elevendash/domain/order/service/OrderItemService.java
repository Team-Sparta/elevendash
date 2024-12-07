package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.service.CartService;
import com.example.elevendash.domain.order.entity.OrderItems;
import com.example.elevendash.domain.order.repository.OrderItemsRepository;
import com.example.elevendash.domain.order.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {


    private final OrderItemsRepository orderItemsRepository;

    public OrderItemService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    public List<OrderItems> addOrderItems(HttpServletRequest request) {
        List<CartRequestDto> cartRequestDto = CartService.getCartFromCookies(request);
        List<OrderItems> orderItemsList = new ArrayList<>();
        for (CartRequestDto dto : cartRequestDto) {
            OrderItems orderItems = new OrderItems(dto.getMenuName(), dto.getPrice(), dto.getQuantity());
            orderItemsList.add(orderItems);
            orderItemsRepository.save(orderItems);
        }
        return orderItemsList;
    }
}
