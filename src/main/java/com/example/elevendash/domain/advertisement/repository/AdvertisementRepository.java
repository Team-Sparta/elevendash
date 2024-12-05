package com.example.elevendash.domain.advertisement.repository;

import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    boolean existsByStore(@NotNull Store store);
}
