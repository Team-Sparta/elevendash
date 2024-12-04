package com.example.elevendash.domain.cart.service;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.dto.response.CartResponseDto;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.store.entity.Store;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final StoreRepoistory storeRepoistory;
    private final OrderRepository orderRepository;

    public CartService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CartResponseDto createCookie(Long storesId, Long orderId, HttpServletResponse response, CartRequestDto requestDto) {
        Order order = orderRepository.findByOrderId(orderId);
        Store store = storeRepository.findById(storesId);

        if (!order.getStore().getId().equals(storesId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 못한 요청입니다");
        }

        List<Menu> orderMenuName = requestDto.getMenuName();
        List<Menu> orderSuccess = new ArrayList<>();

            for (Menu orderMenu : orderMenuName) {
                if (store.getMenus().contains(orderMenu)) {
                    orderSuccess.add(orderMenu);
                }else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않은 매뉴입니다");
            }
        }
        Cookie cookie = new Cookie("menuName", String.valueOf(requestDto));
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return new CartResponseDto(orderId, orderSuccess);
    }
}
