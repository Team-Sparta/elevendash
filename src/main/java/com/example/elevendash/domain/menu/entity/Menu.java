package com.example.elevendash.domain.menu.entity;

import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

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
    @Column @NotBlank
    @Length(max = 50)
    private String menuName;

    @Column @NotBlank
    @Length(max = 255)
    private String menuDescription;

    @Column @NotNull
    @Min(0)
    private Integer menuPrice;

    @Lob @NotBlank
    @Column
    private String menuImage;
    //연관 관계

    /**
     * store와 1대1 연결
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Store.class)
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * category와 ManyToOne 연결
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Category.class)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * menuOption과 OneToMany 연결
     */
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOption> menuOptions;
    // order와 관계 정리할 필요 있음

    /**
     * 메뉴 생성자
     * @param menuName
     * @param menuPrice
     * @param store
     * @param category
     * @param menuImage
     */
    @Builder
    public Menu(String menuName, Integer menuPrice, Store store, Category category, String menuImage,String menuDescription) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.store = store;
        this.category = category;
        this.menuImage = menuImage;
        this.menuDescription = menuDescription;
    }

    /**
     * 메뉴 변경 메소드
     * @param menuName
     * @param menuPrice
     * @param category
     * @param menuImage
     */

    public void update(String menuName, Integer menuPrice, Category category, String menuImage ,String menuDescription ) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.menuImage = menuImage;
        this.menuDescription = menuDescription;
    }

    protected Menu() {}
}
