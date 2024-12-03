package com.example.elevendash.domain.store.entity;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "stores")
public class Store extends BaseTimeEntity {
    /**
     * 식별자 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false, name = "store_id")
    private Long id;

    /**
     * 필드 변수 설정
     */
    @Column(nullable = false)
    private String storeName;

    @Lob @Column
    private String storeDescription;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private String storePhone;

    @Column(nullable = false)
    private Integer leastAmount;

    @Lob @Column
    private String storeImage;

    @Column
    private Boolean isDeleted = Boolean.FALSE;

    // 연관 관계 설정

    /**
     * 멤버와 manyToOne 연결
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 메뉴와 OneToMany 연결
     */
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();
    /**
     * 주문과 OneToMany 연결
     */
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    /**
     * 리뷰와 OneToMany 연결
     */
//    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Review> reviews = new ArrayList<>();

    /**
     * Store 생성자
     * @param storeName
     * @param storeDescription
     * @param storeAddress
     * @param storePhone
     * @param leastAmount
     * @param storeImagem
     * @param member
     */
    public Store(String storeName, String storeDescription, String storeAddress, String storePhone, Integer leastAmount, String storeImagem, Member member) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
        this.leastAmount = leastAmount;
        this.storeImage = storeImagem;
        this.member = member;
    }

    /**
     * Store 수정 메소드
     * @param storeName
     * @param storeDescription
     * @param storeAddress
     * @param storePhone
     * @param leastAmount
     */
    public void update(String storeName, String storeDescription, String storeAddress, String storePhone, Integer leastAmount ) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
        this.leastAmount = leastAmount;
    }

    /**
     * Store soft delete 메소드
     */
    public void delete() {
        isDeleted = true;
    }

    /**
     * 기본 생성자
     */
    protected Store() {}






}
