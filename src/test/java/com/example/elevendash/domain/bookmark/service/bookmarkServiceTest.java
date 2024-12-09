package com.example.elevendash.domain.bookmark.service;

import com.example.elevendash.domain.bookmark.repository.BookMarkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

//@ExtendWith(MockitoExtension.class)
//public class bookmarkServiceTest {
//    @Mock
//    BookMarkRepository bookMarkRepository;
//
//    @InjectMocks
//
//    @Test
//    @DisplayName("포인트 전환 계산")
//    void calculatePoint() {
//        // given
//        Integer foodPrice = 3000;
//        pointService.calculatePoint(foodPrice);
//
//        // when
//        var calculatedPoint = pointService.calculatePoint(foodPrice);
//
//        // then
//        assertEquals(90, calculatedPoint);
//    }
//
//    @Test
//    @DisplayName("멤버의 통합 포인트")
//    void getTotalPoints() {
//        // given
//        Long memberId = 1L;
//        given(pointRepository.getTotalActivePoints(eq(memberId), any(LocalDateTime.class))).willReturn(10L);
//
//        // when
//        var totalActivePoints = pointService.getTotalPoints(memberId);
//
//        // then
//        assertEquals(10L, totalActivePoints.totalAmount());
//    }
//}
