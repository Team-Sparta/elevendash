package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.order.repository.OrderItemsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {


    private final OrderItemsRepository orderItemsRepository;

    public OrderItemService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

//    public List<OrderItems> addOrderItems(HttpServletRequest request) {
//        List<CartMenuInfo> cartRequestDto = CartService.getCartFromCookies(request);
//        List<OrderItems> orderItemsList = new ArrayList<>();
//        for (CartMenuInfo dto : cartRequestDto) {
//            OrderItems orderItems = new OrderItems(dto.getMenuName(), dto.getPrice(), dto.getQuantity());
//            orderItemsList.add(orderItems);
//            orderItemsRepository.save(orderItems);
//        }
//        return orderItemsList;
//    }
}
