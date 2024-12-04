package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
