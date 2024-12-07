package com.example.elevendash.domain.cart.service;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CartService {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper jacksonObjectMapper;


    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CART_COOKIE_NAME = "cart";

    public static ArrayList<CartRequestDto> getCartFromCookies(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> CART_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(cookie ->{
                    try {
                        return objectMapper.readValue(cookie.getValue(), objectMapper.getTypeFactory().constructCollectionType(List.class, Menu.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return new ArrayList<CartRequestDto>();
                    }
                })
                .orElse(new ArrayList<>());
    }

    public static void saveCartToCookies(HttpServletResponse response, List<CartRequestDto> cart) {
        try {
            String cartJson = objectMapper.writeValueAsString(cart);
            Cookie cartCookie = new Cookie(CART_COOKIE_NAME, cartJson);
            cartCookie.setPath("/");
            cartCookie.setHttpOnly(true);
            cartCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cartCookie);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<CartRequestDto> getCart(HttpServletRequest request) {
        return getCartFromCookies(request);
    }

    public Store findStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
        return store;
    }

    public List<CartRequestDto> addMenuToCart (HttpServletRequest request, HttpServletResponse response, List<CartRequestDto> cartRequestDto) {
        List<CartRequestDto> selectedItemList = getCartFromCookies(request);
        for (CartRequestDto newMenu : cartRequestDto) {
            for (CartRequestDto selectedMenu : selectedItemList) {
                Long storeId = newMenu.getStoreId();
                Store store = findStoreById(storeId);
                for (Menu menu : store.getMenus()) {
                    if (menu.getMenuName().equals(newMenu.getMenuName()));
                }


                if (!newMenu.getStoreId().equals(selectedMenu.getStoreId())) {
                    cartRequestDto.removeAll(selectedItemList);
                    selectedItemList.add(newMenu);
                } else if (newMenu.getMenuName().equals(selectedMenu.getMenuName())) {
                    newMenu.setQuantity(newMenu.getQuantity() + selectedMenu.getQuantity());
                } 
            }
            selectedItemList.add(newMenu);
        }
        saveCartToCookies(response, selectedItemList);
        return selectedItemList;
    }

    public Long calculateTotal(HttpServletRequest request) {
        List<CartRequestDto> cart = getCartFromCookies(request);
        return cart.stream().mapToLong(CartRequestDto -> CartRequestDto.getPrice() * CartRequestDto.getQuantity()).sum();
    }

}
