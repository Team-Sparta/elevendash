package com.example.elevendash.domain.menu.repository;

import com.example.elevendash.domain.menu.dto.CategoryInfo;
import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(Categories categoryName);

    // 상점 조회시 필요한 정보들을 조회하는 쿼리
    @Query("select c.categoryName, m.menuName, m.menuDescription, m.menuPrice, m.menuImage from Category c left join Menu m on c.id = m.category.id where m.store = :store ")
    List<Object[]> findAllCategoryInfo(@Param("store") Store store);
}
