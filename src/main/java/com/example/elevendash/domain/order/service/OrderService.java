package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse;
import com.example.elevendash.domain.dashboard.enums.PeriodType;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.order.dto.request.CancelOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.orderStatusRequestDto;
import com.example.elevendash.domain.order.dto.response.CancelOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.example.elevendash.domain.member.enums.MemberRole.OWNER;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderService) {
        this.orderRepository = orderService;
    }

    public Order findOrderById(Long ordersId) {
        return orderRepository.findById(ordersId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
    }

    public OrderCheckResponseDto orderDetails(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getOrderStatus().equals("주문 거절")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다");
        }
        return OrderCheckResponseDto.toDto(findOrderById(orderId));
    }

    public StatisticsResponse getStatistics(PeriodType periodType, LocalDate startDate, LocalDate endDate, Long storeId, Categories categories) {

        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일이 시작일보다 앞설 수 없습니다");
        }

        LocalDate now = LocalDate.now();

        switch (periodType) {
            case DAILY -> {
                startDate = now.minusDays(1);
                endDate = now;
            }
            case MONTHLY -> {
                startDate = now.minusMonths(1);
                endDate = now;
            }
            case ANNUAL -> {
                startDate = now.minusYears(1);
                endDate = now;
            }
        }

        LocalDateTime startDateTime = startDate != null ?
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        LocalDateTime endDateTime = endDate != null ?
                endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        return orderRepository.getStatistics(startDateTime, endDateTime, storeId);
    }

    public String orderStatus (Long ordersId, orderStatusRequestDto requestDto) {
        Order order = findOrderById(ordersId);

        Member member = order.getMember();

        if (!member.getRole().equals(OWNER)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 이용 가능합니다");
        }

        String orderStatus = requestDto.getOrderStatus();

        if (order.getOrderStatus().equals(orderStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 상태로는 변경 불가능합니다");
        }

        if (order.getOrderStatus().equals("주문 거절")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "거절한 주문입니다");
        }

        switch (orderStatus) {
            case "주문 수락" -> {
                order.updateStatus("주문 수락");
            }
            case "조리중" -> {
                order.updateStatus("조리중");
            }
            case "조리 완료" -> {
                order.updateStatus("조리 완료");
            }
            case "배달중" -> {
                order.updateStatus("배달중");
            }
            case "배달 완료" -> {
                order.updateStatus("배달 완료");
            }
            default -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못 입력하였습니다 다시 시도해주세요");
            }
        }

        return "변경이 완료되었습니다";
    }

    public CancelOrderResponseDto cancelOrder (Long ordersId, CancelOrderRequestDto requestDto) {
        Order order = findOrderById(ordersId);
        order.updateStatus("주문 거절");
        return new CancelOrderResponseDto(order.getOrderStatus(), requestDto.getCancelMassage());
    }
}
