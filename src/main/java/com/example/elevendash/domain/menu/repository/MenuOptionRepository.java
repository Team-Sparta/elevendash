package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.dto.MenuOptionInfo;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {

    List<MenuOption> findByMenu(Menu menu);
}
