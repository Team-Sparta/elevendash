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
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AddOrderResponseDto> addOrder (@RequestBody @Valid AddOrderRequestDto requestDto, HttpServletRequest servletRequest,
                                                         @LoginMember Member loginMember) {
        AddOrderResponseDto responseDto = null;
        try {
            responseDto = orderService.addOrder(requestDto, servletRequest, loginMember);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDto);
    }
}
