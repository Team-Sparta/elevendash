package com.example.elevendash.domain.cart.controller;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.dto.response.CartResponseDto;
import com.example.elevendash.domain.cart.service.CartService;
import com.example.elevendash.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final OrderService orderService;
    private final CartService cartService;
    private final HttpMessageConverters messageConverters;

    @PostMapping("/crats")
    public ResponseEntity<CartResponseDto> addCart(HttpServletResponse response, HttpServletRequest request, @Validated CartRequestDto requestDto) {
        return ResponseEntity.ok().body(cartService.createCookie(response, request, requestDto));
    }

}
