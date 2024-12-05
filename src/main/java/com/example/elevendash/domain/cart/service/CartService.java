package com.example.elevendash.domain.cart.service;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.dto.response.CartResponseDto;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    public CartService(StoreRepository storeRepository, OrderRepository orderRepository) {
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
    }

    public CartResponseDto createCookie(Long orderId, HttpServletResponse response, CartRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("잘못된 Id 값입니다"));
        List<Menu> orderMenuName = requestDto.getMenuName();
        Store storeId = findstoreByOrder(orderId);
        if (!order.getStore().getId().toString().equals(storeId.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 못한 요청입니다");
        }

        List<Menu> orderSuccess = new ArrayList<>();

            for (Menu orderMenu : orderMenuName) {
                if (storeId.getMenus().contains(orderMenu)) {
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

    public Store findstoreByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("잘못된 Id 값입니다"));
        Store storeId = order.getStore();
        return storeId;
    }
}
