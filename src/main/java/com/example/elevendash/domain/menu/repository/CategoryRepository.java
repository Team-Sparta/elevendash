package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
