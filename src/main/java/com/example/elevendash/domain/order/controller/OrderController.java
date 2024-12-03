package com.example.elevendash.domain.order.controller;

import com.example.elevendash.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    OrderService orderService;

//    @PostMapping("/{orderId}/cart")
//    public ResponseEntity<cartResponseDto> addCart(@PathVariable String orderId) {
//        return null;
//    }
}
