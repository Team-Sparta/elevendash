package com.example.elevendash.domain.cart.service;

import com.example.elevendash.domain.cart.dto.CartInfo;
import com.example.elevendash.domain.cart.dto.CartMenuInfo;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CartService {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper jacksonObjectMapper;


    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CART_COOKIE_NAME = "cart";

    private CartInfo getCartFromCookies(HttpServletRequest request, HttpServletResponse response)throws JsonProcessingException{
        if(request.getCookies() == null) {
            return new CartInfo(new ArrayList<>());
        }
        Cookie cartCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(CART_COOKIE_NAME)).findFirst().orElse(null);
        if(cartCookie == null) {
            return new CartInfo(new ArrayList<>());
        }
        String cartString;
        try {
            cartString = decodeCartCookie(cartCookie);
        }catch (Exception e) {
            throw new BaseException(ErrorCode.NOT_FOUND_CART);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        CartInfo cartInfo = objectMapper.readValue(cartString, new TypeReference<CartInfo>() {});


        return cartInfo;
    }



    public void addMenuToCart (HttpServletRequest request, HttpServletResponse response, CartMenuInfo cartMenuInfo)throws JsonProcessingException {
        String encodedJson = null;

        CartInfo selectedItemList = getCartFromCookies(request,response);

        if(selectedItemList != null) {
            selectedItemList.getCartMenus().add(cartMenuInfo);
            try {
                String jsonString = objectMapper.writeValueAsString(selectedItemList);
                encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                Cookie cookie = new Cookie(CART_COOKIE_NAME, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
                throw new BaseException(ErrorCode.NOT_FOUND_CART);
            }
        }
        else{
            selectedItemList = new CartInfo(new ArrayList<>());
            selectedItemList.getCartMenus().add(cartMenuInfo);
        }

        // 쿠키에 저장할때 "가 들어가서 오류 발생
        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, encodedJson);
        cartCookie.setPath("/");
        cartCookie.setHttpOnly(true);
        cartCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cartCookie);
    }
    private String decodeCartCookie(Cookie cartCookie) throws Exception {
        // 쿠키 값 디코딩
        return URLDecoder.decode(cartCookie.getValue(), StandardCharsets.UTF_8.toString());
    }

}
