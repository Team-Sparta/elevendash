package com.example.elevendash.domain.menu.entity;

import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "menu_options"
)
public class MenuOption extends BaseTimeEntity {
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
    @NotBlank
    private String content;

    @Column
    @NotNull
    @Min(0)
    private Integer optionPrice;

    @Column
    @NotNull
    private Boolean selected = Boolean.FALSE;

    // 연관 관계 설정

    /**
     * Menu와 ManyToOne 관계
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Menu.class)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    /**
     * 생성자
     * @param content
     * @param menu
     */
    public MenuOption(String content, Integer optionPrice ,Menu menu) {
        this.content = content;
        this.optionPrice = optionPrice;
        this.menu = menu;
    }

    /**
     * MenuOption 수정 메소드
     * @param content
     */
    public void update(String content, Integer optionPrice) {
        this.content = content;
        this.optionPrice = optionPrice;
    }

    public void select(Boolean selected) {
        this.selected = selected;
    }

    /**
     * 기본 생성자
     */
    protected MenuOption() {}

}
