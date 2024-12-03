package com.example.elevendash.domain.menu.entity;

import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MenuOption extends BaseTimeEntity {
    /**
     * 식별자 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_option_id", nullable = false, updatable = false, unique = true)
    private Long id;
    /**
     * 필드 변수
     */
    @Column
    private String content;

    @Column
    private Integer number;

    // 연관 관계 설정

    /**
     * Menu와 ManyToOne 관계
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Menu.class)
    private Menu menu;

    /**
     * 생성자
     * @param content
     * @param number
     * @param menu
     */
    public MenuOption(String content, Integer number, Menu menu) {
        this.content = content;
        this.number = number;
        this.menu = menu;
    }

    /**
     * MenuOption 수정 메소드
     * @param content
     * @param number
     */
    public void update(String content, Integer number) {
        this.content = content;
        this.number = number;
    }

    /**
     * 기본 생성자
     */
    protected MenuOption() {}

}
