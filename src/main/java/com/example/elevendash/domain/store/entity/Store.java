package com.example.elevendash.domain.store.entity;

import com.example.elevendash.domain.bookmark.entity.Bookmark;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {
    /**
     * 식별자 생성
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 필드 변수 설정
     */
    @Column(nullable = false)
    @NotBlank
    private String storeName;

    @Lob
    @Column @NotBlank
    private String storeDescription;

    @Column @NotNull
    private LocalTime openTime;

    @Column @NotNull
    private LocalTime closeTime;

    @Column(nullable = false) @NotBlank
    private String storeAddress;

    @Column(nullable = false) @NotBlank
    private String storePhone;

    @Column(nullable = false) @NotNull
    @Min(value = 0)
    private Integer leastAmount;

    @Lob
    @Column @NotBlank
    private String storeImage;

    @Column
    private Boolean isDeleted = Boolean.FALSE;

    // 연관 관계 설정

    /**
     * 멤버와 manyToOne 연결
     */
    @ManyToOne
    @JoinColumn(name = "member_id") @NotNull
    private Member member;

    /**
     * 메뉴와 OneToMany 연결
     */
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();
    /**
     * 주문과 OneToMany 연결
     */
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    /**
     * 리뷰와 OneToMany 연결
     */
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    /**
     *
     * @param storeName
     * @param storeDescription
     * @param storeAddress
     * @param storePhone
     * @param leastAmount
     * @param storeImage
     * @param openTime
     * @param closeTime
     * @param member
     */
    @Builder
    public Store(String storeName, String storeDescription, String storeAddress, String storePhone, Integer leastAmount, String storeImage, LocalTime openTime, LocalTime closeTime, Member member) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
        this.leastAmount = leastAmount;
        this.storeImage = storeImage;
        this.member = member;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    /**
     * Store 수정 메소드
     *
     * @param storeName
     * @param storeDescription
     * @param storeAddress
     * @param storePhone
     * @param leastAmount
     */
    public void update(String storeName, String storeDescription, String storeAddress, String storePhone, Integer leastAmount, String storeImage, LocalTime openTime, LocalTime closeTime) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
        this.leastAmount = leastAmount;
        this.storeImage = storeImage;
        this.openTime = openTime;
        this.closeTime = closeTime;
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







}
