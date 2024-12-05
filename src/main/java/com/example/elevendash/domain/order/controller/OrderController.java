package com.example.elevendash.domain.order.controller;

import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{ordersId}")
    public ResponseEntity<OrderCheckResponseDto> orderDetails(@PathVariable Long ordersId) {
        return ResponseEntity.ok().body(orderService.orderDetails(ordersId));
    }
}
