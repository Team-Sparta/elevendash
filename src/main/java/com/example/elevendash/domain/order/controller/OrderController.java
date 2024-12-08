package com.example.elevendash.domain.order.controller;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.order.dto.request.AddOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.CancelOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.orderStatusRequestDto;
import com.example.elevendash.domain.order.dto.response.AddOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.CancelOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.service.OrderService;
import com.example.elevendash.global.annotation.LoginMember;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
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
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{ordersId}")
    public ResponseEntity<OrderCheckResponseDto> orderDetails(@PathVariable Long ordersId, @LoginMember Member loginMember) {
        return ResponseEntity.ok().body(orderService.orderDetails(ordersId));
    }

    @PatchMapping("/{ordersId}/order-status")
    public ResponseEntity<String> changeOrderStatus (@PathVariable Long ordersId, orderStatusRequestDto requestDto,
                                                     @LoginMember Member loginMember) {
        return ResponseEntity.ok().body(orderService.orderStatus(ordersId, requestDto, loginMember));
    }

    @PatchMapping("/{ordersId}/cancelOrder")
    public ResponseEntity<CancelOrderResponseDto> cancelOrder (@PathVariable Long ordersId, CancelOrderRequestDto requestDto, @LoginMember Member loginMember) {
        return ResponseEntity.ok().body(orderService.cancelOrder(ordersId,requestDto));
    }

    @PostMapping
    public ResponseEntity<AddOrderResponseDto> addOrder (@RequestBody @Valid AddOrderRequestDto requestDto, HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                                         @LoginMember Member loginMember) {
        try {
            AddOrderResponseDto responseDto = orderService.addOrder(requestDto, servletRequest, loginMember, servletResponse);
            return ResponseEntity.ok().body(responseDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
