package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByIdIn(List<Long> ids);
}
