package com.example.elevendash.domain.advertisement.repository;

import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.advertisement.enums.AdvertisementStatus;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    boolean existsByStore(@NotNull Store store);

    List<Advertisement> findAllByMember(@NotNull Member member);

    @Query("select a from Advertisement a where a.status = :status order by a.bidPrice desc ")
    List<Advertisement> findAllByStatusOrderPrice (@Param("status") AdvertisementStatus status);
}
