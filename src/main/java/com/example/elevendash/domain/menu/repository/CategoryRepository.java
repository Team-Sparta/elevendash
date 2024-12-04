package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.enums.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(Categories categoryName);
}
