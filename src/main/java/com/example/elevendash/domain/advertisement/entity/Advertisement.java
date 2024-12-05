package com.example.elevendash.domain.advertisement.entity;

import com.example.elevendash.domain.advertisement.enums.AdvertisementState;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.global.entity.BaseTimeEntity;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "advertisements", uniqueConstraints = @UniqueConstraint(columnNames = {"member","store"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advertisement extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    @NotNull @Min(0)
    private Integer bidPrice;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private AdvertisementState state = AdvertisementState.WAITING;

    @Column
    private String rejectReason;

    /**
     * 연관 관계 설정
     */

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Member.class)
    @NotNull
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * store과 one to one 관계 store쪽이 주인
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Store.class)
    @NotNull
    @JoinColumn(name = "store_id")
    private Store store;
    // 광고 제안
    public Advertisement(Integer bidPrice,Member member, Store store) {
           this.bidPrice = bidPrice;
           this.member = member;
           this.store = store;
    }
    // 광고 금액 수정
    public void retryBid(Integer bidPrice) {
        this.bidPrice = bidPrice;
        this.state = AdvertisementState.WAITING;
    }
    // 광고  거절
    public void rejectBid(String rejectReason) {
        if (this.state != AdvertisementState.WAITING) {
            throw new BaseException(ErrorCode.NOT_STATUS_WAITING);
        }
        this.rejectReason = rejectReason;
        this.state = AdvertisementState.REJECTED;
    }
    // 상점 주인의 광고 종료
    public void stop(){
        if (this.state != AdvertisementState.ACCEPTED) {
            throw new BaseException(ErrorCode.NOT_STATUS_ACCEPTED);
        }
        this.state = AdvertisementState.STOPPED;
    }

}
