package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.cart.dto.response.CartCookieResponseDto;
import com.example.elevendash.domain.coupon.entity.Coupon;
import com.example.elevendash.domain.coupon.entity.CouponUsage;
import com.example.elevendash.domain.coupon.repository.CouponRepository;
import com.example.elevendash.domain.coupon.service.CouponService;
import com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse;
import com.example.elevendash.domain.dashboard.enums.PeriodType;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.menu.repository.MenuRepository;
import com.example.elevendash.domain.order.dto.request.AddOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.CancelOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.orderStatusRequestDto;
import com.example.elevendash.domain.order.dto.response.AddOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.CancelOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.point.repository.PointRepository;
import com.example.elevendash.domain.point.service.PointService;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForOffsetDateTime;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.example.elevendash.domain.member.enums.MemberRole.OWNER;
import static com.example.elevendash.domain.member.enums.MemberRole.valueOf;

@Service
public class OrderService {

    private final ObjectMapper jacksonObjectMapper;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final PointRepository pointRepository;


    public OrderService(ObjectMapper jacksonObjectMapper, OrderRepository orderService, MemberRepository memberRepository, MenuRepository menuRepository, StoreRepository storeRepository, CouponRepository couponRepository, CouponService couponService, PointRepository pointRepository) {
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.orderRepository = orderService;
        this.memberRepository = memberRepository;
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        this.couponRepository = couponRepository;
        this.couponService = couponService;
        this.pointRepository = pointRepository;

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

    public AddOrderResponseDto addOrder (AddOrderRequestDto requestDto, HttpServletRequest request) throws JsonProcessingException {
        Long memberId = requestDto.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("잘못된 Id 값입니다"));
        Long menuPrice = 0L;
        BigDecimal totalPrice = new BigDecimal(0);

        List<Menu> menus = new ArrayList<>();
        List<Long> manuCount = new ArrayList<>();

        Long storeId = 0L;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("menuList")) {
                Map<String, CartCookieResponseDto> menuList = jacksonObjectMapper.readValue(cookie.getValue(), Map.class);
                for (String key : menuList.keySet()) {
                    CartCookieResponseDto cartCookieResponseDto = menuList.get(key);
                    menus.addAll(cartCookieResponseDto.getMenus());
                    manuCount.addAll(cartCookieResponseDto.getMenuCount());

                }
            }else if (cookie.getName().equals("store_id")) {
                Long storeIda = jacksonObjectMapper.readValue(cookie.getValue(), Long.class);
                storeId = storeIda;
            }
        }
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));



        // 금액 구하는 for문
        for (Menu singleMenuId : menus) {
            Menu menu = menuRepository.findById(singleMenuId.getId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Id 값입니다"));
            menuPrice = Long.valueOf(menu.getMenuPrice());
        }

        // 구한 금액을 쿠폰에 적용
        List<CouponUsage> membercoupon = member.getCoupons();
        for (CouponUsage singleCoupon : membercoupon) {
            if (singleCoupon.getId().equals(requestDto.getCouponId())) {
                Coupon coupon = couponRepository.findById(singleCoupon.getId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Id 값입니다"));
                BigDecimal bigDecimalPrice = BigDecimal.valueOf(menuPrice);
                //할인 쿠폰 적용 (BigDecimal로 반환)
                totalPrice = couponService.calculateDiscount(coupon, bigDecimalPrice);
                singleCoupon.useCoupon();
            }
        }

        // BigDecimal 타입인 totalPrice 를 Long 타입으로 변경
        Long LongTotalPrice = Long.valueOf(String.valueOf(totalPrice));

        Order order = new Order(LongTotalPrice, menus, member, store);
        Order saveOrder = orderRepository.save(order);
        return AddOrderResponseDto.toDto(saveOrder);
    }
}
