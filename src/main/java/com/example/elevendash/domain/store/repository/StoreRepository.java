package com.example.elevendash.domain.store.repository;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.dto.CategoryInfo;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.store.dto.StoreInfo;
import com.example.elevendash.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {


    Long countByMemberAndIsDeleted(Member member, Boolean isDeleted);

    Optional<Store> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    // 그냥 조회
    Page<Store> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
    // 북마크순 조회
    @Query("select distinct s from Store s left join s.bookmarks b where s.isDeleted = :isDeleted group by s.id order by count(b) desc ")
    Page<Store> findAllByIsDeletedBookmarkSort(Boolean isDeleted, Pageable pageable);
    // 내가 북마크한 상점 조회
    @Query("select distinct s from Member m left join m.bookmarks b left join b.store s where s.isDeleted = :isDeleted and b.member = :loginMember group by s.id")
    Page<Store> findAllByIsDeletedAndMyBookmark(Boolean isDeleted, Member loginMember, Pageable pageable);

    @Query("select s from Store s left join s.menus m left join m.category c where s.isDeleted = :isDeleted and c.categoryName = :category group by s.id")
    Page<Store> findAllByIsDeletedAndCategory(Boolean isDeleted, Categories category, Pageable pageable);

    List<Store> findAllByMemberAndIsDeleted(@NotNull Member member, Boolean isDeleted);

    Optional<Store> findByIdAndIsDeletedAndMember(Long id, Boolean isDeleted, @NotNull Member member);
}
