package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    Long menu(Menu menu);
}
