package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.enums.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(Categories categoryName);
}
