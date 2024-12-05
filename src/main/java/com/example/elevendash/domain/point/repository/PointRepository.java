package com.example.elevendash.domain.point.repository;

import com.example.elevendash.domain.point.dto.response.TotalPointsResponse;
import com.example.elevendash.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("SELECT p FROM Point p WHERE p.memberId = :memberId AND p.expirationDate > :now AND p.amount > 0")
    List<Point> findActivePoints(@Param("memberId") Long memberId, @Param("now") LocalDateTime now);

    @Query("SELECT new com.example.elevendash.domain.point.dto.response.TotalPointsResponse(SUM(p.amount)) " +
            "FROM Point p " +
            "WHERE p.memberId = :memberId AND p.expirationDate > :now AND p.amount > 0")
    TotalPointsResponse getTotalActivePoints(@Param("memberId") Long userId, @Param("now") LocalDateTime now);

    List<Point> findByExpirationDateBefore(LocalDateTime now);
}
