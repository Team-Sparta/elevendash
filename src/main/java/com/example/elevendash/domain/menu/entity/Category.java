package com.example.elevendash.domain.menu.entity;

import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(
        name = "categories"
)
public class Category extends BaseTimeEntity {
    /**
     * 식별자 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 필드 변수
     * Category는 한번 생성후 변경 불가
     */
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false,updatable = false)
    private Categories categoryName;

    /**
     * 연관관계 설정
     */

    /**
     * 메뉴와 OneToMany 연결
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menus;

    /**
     * Category 생성자
     * @param categoryName
     */
    public Category(Categories categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 기본 생성자
     */
    protected Category() {}
}
