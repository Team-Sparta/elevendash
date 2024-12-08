package com.example.elevendash.domain.order.service;

import com.example.elevendash.domain.cart.dto.CartInfo;
import com.example.elevendash.domain.cart.dto.CartMenuInfo;
import com.example.elevendash.domain.coupon.entity.Coupon;
import com.example.elevendash.domain.coupon.entity.CouponUsage;
import com.example.elevendash.domain.coupon.enums.CouponType;
import com.example.elevendash.domain.coupon.repository.CouponRepository;
import com.example.elevendash.domain.coupon.repository.CouponUsageRepository;
import com.example.elevendash.domain.coupon.service.CouponService;
import com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse;
import com.example.elevendash.domain.dashboard.enums.PeriodType;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.repository.MemberRepository;
import com.example.elevendash.domain.menu.dto.MenuOptionInfo;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.entity.MenuOption;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.menu.repository.MenuOptionRepository;
import com.example.elevendash.domain.menu.repository.MenuRepository;
import com.example.elevendash.domain.order.dto.OrderMenuInfo;
import com.example.elevendash.domain.order.dto.request.AddOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.CancelOrderRequestDto;
import com.example.elevendash.domain.order.dto.request.orderStatusRequestDto;
import com.example.elevendash.domain.order.dto.response.AddOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.CancelOrderResponseDto;
import com.example.elevendash.domain.order.dto.response.OrderCheckResponseDto;
import com.example.elevendash.domain.order.dto.response.OrderOptionInfo;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.order.entity.OrderMenu;
import com.example.elevendash.domain.order.entity.OrderMenuOption;
import com.example.elevendash.domain.order.enums.OrderStatus;
import com.example.elevendash.domain.order.repository.OrderItemsRepository;
import com.example.elevendash.domain.order.repository.OrderMenuOptionRepository;
import com.example.elevendash.domain.order.repository.OrderMenuRepository;
import com.example.elevendash.domain.order.repository.OrderRepository;
import com.example.elevendash.domain.point.entity.Point;
import com.example.elevendash.domain.point.repository.PointRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.example.elevendash.domain.member.enums.MemberRole.OWNER;
import static com.example.elevendash.domain.member.enums.MemberRole.valueOf;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ObjectMapper jacksonObjectMapper;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final PointRepository pointRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final OrderMenuOptionRepository orderMenuOptionRepository;
    private final OrderMenuRepository orderMenuRepository;

    private final String CART_COOKIE_NAME = "cart";
    private final CouponUsageRepository couponUsageRepository;


    public Order findOrderById(Long ordersId) {
        return orderRepository.findById(ordersId).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다"));
    }

    public OrderCheckResponseDto orderDetails(Long orderId) {
        Order order = findOrderById(orderId);
        if (OrderStatus.ORDER_REJECTION.equals(order.getOrderStatus())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다");
        }
        List<OrderMenuInfo> orderMenuInfoList= new ArrayList<>();
        for(OrderMenu orderMenu : order.getOrderMenus()){
            List<OrderOptionInfo> menuOptionInfoList = orderMenu.getOptions().stream().map(
                    orderMenuOption -> new OrderOptionInfo(
                            orderMenuOption.getOrderMenu().getId(),
                            orderMenuOption.getMenuOption().getContent(),
                            orderMenuOption.getQuantity()
                    )
            ).toList();
            OrderMenuInfo orderMenuInfo = new OrderMenuInfo(orderMenu.getMenu().getId(),orderMenu.getMenu().getMenuName(), menuOptionInfoList);
            orderMenuInfoList.add(orderMenuInfo);
        }
        return new OrderCheckResponseDto(orderId,order.getPrice(),orderMenuInfoList,order.getOrderStatus());
    }
    @Transactional
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
    @Transactional
    public String orderStatus (Long ordersId, orderStatusRequestDto requestDto, Member member) {
        Order order = findOrderById(ordersId);


        if (!member.getRole().equals(OWNER)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 이용 가능합니다");
        }

        OrderStatus orderStatus = requestDto.getOrderStatus();

        if (order.getOrderStatus().equals(orderStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 상태로는 변경 불가능합니다");
        }

        if (OrderStatus.ORDER_REJECTION.equals(order.getOrderStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "거절한 주문입니다");
        }
        order.updateStatus(orderStatus);

        return "변경이 완료되었습니다";
    }

    public CancelOrderResponseDto cancelOrder (Long ordersId, CancelOrderRequestDto requestDto) {
        Order order = findOrderById(ordersId);
        order.updateStatus(OrderStatus.ORDER_REJECTION);
        return new CancelOrderResponseDto(order.getOrderStatus(), requestDto.getCancelMassage());
    }

    // 포인트 쿠폰 구현 하다말음 주문만 가능
    @Transactional
    public AddOrderResponseDto addOrder (AddOrderRequestDto requestDto, HttpServletRequest request, Member member, HttpServletResponse response) throws JsonProcessingException {

        BigDecimal totalPrice = new BigDecimal(0);
        if (request.getCookies() == null) {
            throw new BaseException(ErrorCode.NOT_FOUND_CART);
        }
        Cookie cartCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("cart")).findFirst().orElse(null);
        if (cartCookie == null) {
            throw new BaseException(ErrorCode.NOT_FOUND_CART);
        }
        String cartString;
        try {
            cartString = decodeCartCookie(cartCookie);
        }catch (Exception e) {
            throw new BaseException(ErrorCode.NOT_FOUND_CART);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        Cookie cookie = new Cookie(CART_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        CartInfo cartInfo = objectMapper.readValue(cartString, new TypeReference<CartInfo>() {});

        Store store = storeRepository.findById(cartInfo.getCartMenus().get(0).getStoreId()).orElse(null);
        if (store == null) {
            throw new BaseException(ErrorCode.NOT_FOUND_STORE);
        }

        Coupon coupon = couponRepository.findById(requestDto.getCouponId()).orElse(null);
        Point point = pointRepository.findByMember(member);

        Order order = new Order(member,store,coupon);

        orderRepository.saveAndFlush(order);
        for(CartMenuInfo cartMenuInfo : cartInfo.getCartMenus()) {
            Menu menu = menuRepository.findById(cartMenuInfo.getMenuId()).orElse(null);
            if (menu == null) {
                continue;
            }
            totalPrice = totalPrice.add(BigDecimal.valueOf(menu.getMenuPrice()));
            OrderMenu orderMenu = new OrderMenu(menu,order);
            orderMenuRepository.saveAndFlush(orderMenu);
            for(CartMenuInfo.menuOptionInfo menuOptionInfo : cartMenuInfo.getMenuOptions()) {
                MenuOption menuOption = menuOptionRepository.findById(menuOptionInfo.getOptionId()).orElse(null);
                if (menuOption == null) {
                    continue;
                }
                totalPrice = totalPrice.add(BigDecimal.valueOf(menuOption.getOptionPrice()));
                OrderMenuOption orderMenuOption = new OrderMenuOption(menuOption,menuOptionInfo.getOptionQuantity(),orderMenu);
                orderMenuOptionRepository.save(orderMenuOption);
            }

        }

        if(point != null) {
            totalPrice = totalPrice.subtract(BigDecimal.valueOf(point.getAmount()));
        }

        if(coupon == null) {
            if(totalPrice.compareTo(BigDecimal.valueOf(0))< 0){
                totalPrice = BigDecimal.ZERO;
            }
            order.updatePrice(totalPrice);
            return new AddOrderResponseDto(order.getId());
        }
        if(CouponType.PERCENTAGE.equals(coupon.getType())){
            BigDecimal discountRate = coupon.getDiscountValue().divide(new BigDecimal(100),3,RoundingMode.HALF_UP);
            totalPrice = totalPrice.multiply(BigDecimal.ONE.subtract(discountRate));
        }
        if(CouponType.FIXED_AMOUNT.equals(coupon.getType())){
            totalPrice = totalPrice.add(coupon.getDiscountValue().multiply(BigDecimal.valueOf(-1)));
        }
        // 가격이 0보다 작을경우 0이된다
        if(totalPrice.compareTo(BigDecimal.valueOf(0))< 0){
            totalPrice = BigDecimal.ZERO;
        }
        CouponUsage couponUsage = new CouponUsage(coupon,member,true);

        couponUsageRepository.save(couponUsage);
        order.updatePrice(totalPrice);
        return new AddOrderResponseDto(order.getId());


    }

    private String decodeCartCookie(Cookie cartCookie) throws Exception {
        // 쿠키 값 디코딩
        return URLDecoder.decode(cartCookie.getValue(), StandardCharsets.UTF_8.toString());
    }
}
