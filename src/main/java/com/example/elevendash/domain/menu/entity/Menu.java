package com.example.elevendash.domain.menu.entity;

import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(
        name = "menus"
)
public class Menu extends BaseTimeEntity {
    /**
     * 식별자 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 필드 변수
     */
    @Column
    private String menuName;

    @Column
    private Integer menuPrice;

    //연관 관계

    /**
     * store와 1대1 연결
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Store.class)
    private Store store;

    /**
     * category와 ManyToOne 연결
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Category.class)
    private Category category;

    /**
     * menuOption과 OneToMany 연결
     */
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOption> menuOptions;
    // order와 관계 정리할 필요 있음

    /**
     * Menu 생성자
     * @param menuName
     * @param menuPrice
     * @param store
     * @param category
     */
    public Menu(String menuName, Integer menuPrice, Store store, Category category) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.store = store;
        this.category = category;
    }

    /**
     * 메뉴 변경 메소드
     * @param menuName
     * @param menuPrice
     * @param store
     * @param category
     */
    public void update(String menuName, Integer menuPrice, Store store, Category category ) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.store = store;
        this.category = category;
    }

    protected Menu() {}
}
