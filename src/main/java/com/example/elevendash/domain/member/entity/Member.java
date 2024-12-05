package com.example.elevendash.domain.member.entity;

import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.bookmark.entity.Bookmark;
import com.example.elevendash.domain.member.dto.request.SignUpRequest;
import com.example.elevendash.domain.member.dto.request.UpdateProfileRequest;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.member.enums.Provider;
import com.example.elevendash.domain.order.entity.Order;
import com.example.elevendash.domain.review.entity.Review;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "members"
)

public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar(255) comment '이메일'")
    private String email;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(45) comment '회원 이름'")
    private String name;

    @Setter
    @Column(name = "password", columnDefinition = "varchar(255) comment '비밀번호'")
    private String password;

    @Column(name = "profile_image", columnDefinition = "varchar(255) comment '프로필 이미지'")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, columnDefinition = "varchar(20) comment '가입 경로'")
    private Provider provider;

    @Column(name = "provider_id", columnDefinition = "varchar(255) comment '공급 고유 번호'")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(20) comment '권한'")
    private MemberRole role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Advertisement> advertisements = new ArrayList<>();

    @Builder
    private Member(SignUpRequest signUpRequest) {
        this.email = signUpRequest.email();
        this.name = signUpRequest.name();
        this.password = signUpRequest.password();
        this.provider = signUpRequest.provider();
        this.providerId = signUpRequest.providerId();
        this.role = signUpRequest.role() != null ? signUpRequest.role() : MemberRole.CUSTOMER;
    }

    public void updateProfile(UpdateProfileRequest request, String profileImageUrl) {
        this.name = request.name();
        this.profileImage = profileImageUrl;
    }

    public void deleteAccount() {
        this.deletedAt = LocalDateTime.now();
        this.profileImage = null;
    }

    public void validateProvider() {
        if (this.provider != Provider.EMAIL && this.password != null && this.providerId == null) {
            throw new BaseException(ErrorCode.BAD_PROVIDER);
        }
        if (this.provider == Provider.EMAIL && this.password == null) {
            throw new BaseException(ErrorCode.BAD_EMAIL);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(this.id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
