package com.example.elevendash.domain.cart.service;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.dto.response.CartResponseDto;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@AllArgsConstructor
public class CartService {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;



    public CartResponseDto createCookie(HttpServletResponse response, HttpServletRequest request, CartRequestDto requestDto) {
        List<Long> menus = new ArrayList<>(requestDto.getMenuId());
        Long storeId = requestDto.getStoreId();
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
        List<String> orderSuccess = new ArrayList<>();
        List<Menu> storeMenu = store.getMenus();
        Integer price;

        // 클라이언트가 보내준 menu가 store에 있는 menu인지 확인
        for (Menu menu : storeMenu) {
            for (String food : menus) {
                if (storeMenu.stream().map(Objects::toString).toList().contains(food)) {
                    orderSuccess.add(food);
                    price = menu.getMenuPrice();

                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않은 매뉴입니다");
                }
            }
        }


        Cookie cookie = new Cookie("menuName", String.valueOf(requestDto));
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return new CartResponseDto(orderSuccess);
    }



    public Store findstoreByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("잘못된 Id 값입니다"));
        Store storeId = order.getStore();
        return storeId;
    }
}
