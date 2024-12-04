package com.example.elevendash.domain.member.repository;

import com.example.elevendash.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmailAndDeletedAtIsNull(String email);
    Member findByEmailAndDeletedAtIsNull(String email);
}
