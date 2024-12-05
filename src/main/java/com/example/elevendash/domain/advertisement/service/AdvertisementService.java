package com.example.elevendash.domain.advertisement.service;

import com.example.elevendash.domain.advertisement.dto.request.AddAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.request.RejectAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.AcceptAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.dto.response.AddAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.dto.response.DeleteAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.dto.response.RejectAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.advertisement.enums.AdvertisementState;
import com.example.elevendash.domain.advertisement.repository.AdvertisementRepository;
import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final StoreRepository storeRepository;

    /**
     * 광고를 추가하는 서비스 메소드
     * @param member
     * @param requestDto
     * @return
     */
    @Transactional
    public AddAdvertisementResponseDto addAdvertisement(Member member, AddAdvertisementRequestDto requestDto) {
        Store advertiseStore = storeRepository.findByIdAndIsDeletedAndMember(requestDto.getStoreId(),Boolean.FALSE,member)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_STORE));
        if (advertisementRepository.existsByStore(advertiseStore)) {
            throw new BaseException(ErrorCode.DUPLICATE_ADVERTISEMENT);
            }
        Advertisement advertisement = new Advertisement(requestDto.getBidPrice(),member, advertiseStore);
        advertisementRepository.save(advertisement);
        return new AddAdvertisementResponseDto(advertisement.getId());
    }

    /**
     * 광고 삭제 기능
     * @param loginMember
     * @param StoreId
     * @param advertisementId
     * @return
     */
    @Transactional
    public DeleteAdvertisementResponseDto deleteAdvertisement(Member loginMember, Long StoreId ,Long advertisementId) {
        // 쿠폰과 상점 일치 확인
        Store store = storeRepository.findByIdAndIsDeleted(StoreId,Boolean.FALSE).orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_STORE));
        if(!advertisementRepository.existsByStore(store)) {
            throw new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT);
        }
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));
        // 쿠폰과 멤버 일치 확인
        if(!advertisement.getStore().getMember().getId().equals(loginMember.getId())) {
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        advertisement.stop();

        return new DeleteAdvertisementResponseDto(advertisement.getId());
    }

    /**
     * 관리자의 광고 거절 메소드
     * @param loginMember
     * @param StoreId
     * @param advertisementId
     * @param requestDto
     * @return
     */
    @Transactional
    public RejectAdvertisementResponseDto rejectAdvertisement(Member loginMember, Long advertisementId, RejectAdvertisementRequestDto requestDto) {
        if(!loginMember.getRole().equals(MemberRole.ADMIN)){
            throw new BaseException(ErrorCode.NOT_ADMIN);
        }
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));
        if(!advertisement.getStatus().equals(AdvertisementState.WAITING)) {
            throw new BaseException(ErrorCode.NOT_STATUS_WAITING);
        }
        advertisement.rejectBid(requestDto.getRejectReason());
        return new RejectAdvertisementResponseDto(advertisement.getId());
    }

    @Transactional
    public AcceptAdvertisementResponseDto acceptAdvertisement(Member loginMember, Long advertisementId) {
        if(!loginMember.getRole().equals(MemberRole.ADMIN)){
            throw new BaseException(ErrorCode.NOT_ADMIN);
        }
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));
        if(!advertisement.getStatus().equals(AdvertisementState.WAITING)) {
            throw new BaseException(ErrorCode.NOT_STATUS_WAITING);
        }
        advertisement.accept();

        return new AcceptAdvertisementResponseDto(advertisement.getId());
    }



}
