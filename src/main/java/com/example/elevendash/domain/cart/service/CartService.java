package com.example.elevendash.domain.cart.service;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.dto.response.CartCookieResponseDto;
import com.example.elevendash.domain.cart.dto.response.CartResponseDto;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CartService {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper jacksonObjectMapper;


    @SneakyThrows
    public CartResponseDto createCookie(HttpServletResponse response, HttpServletRequest request, CartRequestDto requestDto) {
        Cookie[] getCookie = request.getCookies();
        for (Cookie cookieDail : getCookie) {
            if(!cookieDail.getAttribute("store_id").equals(requestDto.getStoreId().toString())) {
                cookieDail.setMaxAge(0);
                cookieDail.setPath("/");
                response.addCookie(cookieDail);
            }
        }


        Long requestDtoStoreId = requestDto.getStoreId();
        Store store = storeRepository.findById(requestDtoStoreId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
        List<String> orderMenu = new ArrayList<>(requestDto.getMenus());
        List<Menu> storeMenu = store.getMenus();
        List<Menu> menuList = new ArrayList<>();
        Integer totalPrice = 0;

        // 클라이언트가 보내준 menu가 store에 있는 menu인지 확인
        for(String menuName : orderMenu) {
            for (Menu storeMenuDetails : storeMenu) {
                if (storeMenuDetails.getMenuName().equals(menuName)) {
                    menuList.add(storeMenuDetails);
                    Integer price = storeMenuDetails.getMenuPrice();
                    totalPrice += price;
                }else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 메뉴입니다");
                }
            }
        }


        CartCookieResponseDto cartCookieResponseDto = new CartCookieResponseDto(menuList, requestDto.getMenuCount(), requestDtoStoreId);
        Map<String, List<CartCookieResponseDto>> jsonTypeMenu = new HashMap<>();
        jsonTypeMenu.put("menuList", Collections.singletonList(cartCookieResponseDto));
        String StringTypeMenu = jacksonObjectMapper.writeValueAsString(jsonTypeMenu);


        Cookie orderMenuCookie = new Cookie("menuList", StringTypeMenu);
        orderMenuCookie.setPath("/");
        orderMenuCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(orderMenuCookie);

        Cookie storeIdCookie = new Cookie("store_id", requestDto.getStoreId().toString());
        orderMenuCookie.setPath("/");
        orderMenuCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(storeIdCookie);

        return new CartResponseDto(store.getId(),requestDto.getMenus(), totalPrice);
    }
}
