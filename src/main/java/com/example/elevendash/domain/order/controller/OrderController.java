package com.example.elevendash.domain.order.controller;

import com.example.elevendash.domain.order.dto.request.AddOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.CancelOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.orderStatusRequestDto;
import com.example.elevendash.domain.order.dto.response.AddOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.CancelOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.enums.OrderStauts;
import com.example.elevendash.domain.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PatchMapping("/{ordersId}/orderStatus")
    public ResponseEntity<String> changeOrderStatus (@PathVariable Long ordersId, orderStatusRequestDto requestDto) {
        return ResponseEntity.ok().body(orderService.orderStatus(ordersId, requestDto));
    }

    @PatchMapping("/{ordersId}/cancelOrder")
    public ResponseEntity<CancelOrderResponseDto> cancelOrder (@PathVariable Long ordersId, CancelOrderRequestDto requestDto) {
        return ResponseEntity.ok().body(orderService.cancelOrder(ordersId,requestDto));
    }

    @PostMapping
    public ResponseEntity<AddOrderResponseDto> addOrder (AddOrderRequestDto requestDto, HttpServletRequest servletRequest) throws JsonProcessingException {
        return ResponseEntity.ok().body(orderService.addOrder(requestDto, servletRequest));
    }
}
