package com.example.elevendash.domain.cart.controller;

import com.example.elevendash.domain.cart.dto.request.CartRequestDto;
import com.example.elevendash.domain.cart.dto.response.CartResponseDto;
import com.example.elevendash.domain.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/carts")
    public ResponseEntity<CartResponseDto> addCart(HttpServletResponse response, HttpServletRequest request,@CookieValue  @Validated CartRequestDto requestDto) {
        return ResponseEntity.ok().body(cartService.createCookie(response, request, requestDto));
    }
}
