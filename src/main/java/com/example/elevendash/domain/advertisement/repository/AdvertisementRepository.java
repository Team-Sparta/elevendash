package com.example.elevendash.domain.advertisement.repository;

import com.example.elevendash.domain.advertisement.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
