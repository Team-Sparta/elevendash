package com.example.elevendash.domain.order.repository;

import com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse;
import com.example.elevendash.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
             SELECT new com.example.elevendash.domain.dashboard.dto.response.StatisticsResponse(
                 COUNT(o),
                 SUM(o.price)
             )
             FROM Order o
             WHERE (:startDate IS NULL OR o.createdAt >= :startDate)
               AND (:endDate IS NULL OR o.createdAt <= :endDate)
               AND (:storeId IS NULL OR o.store.id = :storeId)
            """)
    StatisticsResponse getStatistics(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     @Param("storeId") Long storeId);
}
