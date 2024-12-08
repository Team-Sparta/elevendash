package com.example.elevendash.domain.cart.controller;


import com.example.elevendash.domain.cart.dto.CartMenuInfo;
import com.example.elevendash.domain.cart.service.CartService;
import com.example.elevendash.domain.order.dto.response.AddOrderResponseDto;
import com.example.elevendash.global.exception.code.SuccessCode;
import com.example.elevendash.global.response.CommonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;


    @PostMapping()
    public ResponseEntity<CommonResponse<Object>> addCart(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid CartMenuInfo cartMenuInfo) {
        try {
            cartService.addMenuToCart(request, response, cartMenuInfo);
            return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
