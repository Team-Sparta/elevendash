package com.example.elevendash.domain.point.repository;

import com.example.elevendash.domain.point.entity.PointHistory;
import com.example.elevendash.domain.point.enums.PointType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    PointHistory findFirstByMemberIdAndAmountAndDescriptionAndTypeOrderByCreatedAtDesc(Long id, Double amount, String description, PointType pointType);
}
