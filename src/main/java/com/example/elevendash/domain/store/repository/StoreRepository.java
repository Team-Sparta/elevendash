package com.example.elevendash.domain.store.repository;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {


    Long countByMemberAndIsDeleted(Member member, Boolean isDeleted);

    Optional<Store> findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
