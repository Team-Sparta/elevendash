package com.example.elevendash.domain.bookmark.entity;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "bookmarks",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id","store_id"})
})
public class Bookmark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    Store store;

    public Bookmark(Member member, Store store) {
        this.member = member;
        this.store = store;
    }
    public Bookmark(){

    }
}
