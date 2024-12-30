package com.example.elevendash.domain.advertisement.service;

import com.example.elevendash.domain.advertisement.dto.request.AddAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.request.RejectAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.request.UpdateAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.*;
import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.advertisement.enums.AdvertisementStatus;
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

import java.util.List;

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
     * @param advertisementId
     * @return
     */
    @Transactional
    public DeleteAdvertisementResponseDto deleteAdvertisement(Member loginMember ,Long advertisementId) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));

        if(!advertisement.getStore().getMember().getId().equals(loginMember.getId())) {
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        advertisement.stop();

        return new DeleteAdvertisementResponseDto(advertisement.getId());
    }

    /**
     * 관리자의 광고 거절 메소드
     * @param loginMember
     * @param advertisementId
     * @param requestDto
     * @return
     */
    @Transactional
    public RejectAdvertisementResponseDto rejectAdvertisement(Member loginMember, Long advertisementId, RejectAdvertisementRequestDto requestDto) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));
        if(!advertisement.getStatus().equals(AdvertisementStatus.WAITING)) {
            throw new BaseException(ErrorCode.NOT_STATUS_WAITING);
        }
        advertisement.rejectBid(requestDto.getRejectReason());
        return new RejectAdvertisementResponseDto(advertisement.getId());
    }

    /**
     * 관리자 광고 수락 기능
     * @param loginMember
     * @param advertisementId
     * @return
     */
    @Transactional
    public AcceptAdvertisementResponseDto acceptAdvertisement(Member loginMember, Long advertisementId) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));
        if(!advertisement.getStatus().equals(AdvertisementStatus.WAITING)) {
            throw new BaseException(ErrorCode.NOT_STATUS_WAITING);
        }
        advertisement.accept();

        return new AcceptAdvertisementResponseDto(advertisement.getId());
    }

    /**
     * 관리자 광고 조회 기능
     * @param loginMember
     * @return
     */
    @Transactional
    public FindAllAdvertisementResponseDto findAllAdvertisement(Member loginMember) {
        List<Advertisement> advertisements = advertisementRepository.findAllByStatusOrderByBidPriceDesc(AdvertisementStatus.WAITING);
        List<FindAllAdvertisementResponseDto.AdvertisementInfo> advertisementInfos =
                advertisements.stream().map(advertisement -> new FindAllAdvertisementResponseDto.AdvertisementInfo(
                    advertisement.getId(),
                    advertisement.getStore().getId(),
                    advertisement.getMember().getId(), advertisement.getBidPrice()))
                        .toList();
        return new FindAllAdvertisementResponseDto(advertisementInfos);
    }

    /**
     * 자신의 광고 조회
     * @param loginMember
     * @return
     */
    @Transactional
    public FindAllMyAdvertisementResponseDto findAllMyAdvertisement(Member loginMember) {
        List<Advertisement> advertisements = advertisementRepository.findAllByMember(loginMember);
        List<FindAllMyAdvertisementResponseDto.AdvertisementInfoForOwner> advertisementInfoForOwnerList
                = advertisements.stream().map(advertisement -> new FindAllMyAdvertisementResponseDto.AdvertisementInfoForOwner(
                        advertisement.getId(),
                advertisement.getStore().getId(),
                advertisement.getRejectReason(),
                advertisement.getBidPrice(),
                advertisement.getStatus()
        )).toList();
        return new FindAllMyAdvertisementResponseDto(advertisementInfoForOwnerList);
    }

    @Transactional
    public UpdateAdvertisementResponseDto updateAdvertisement(Member member,Long advertisementId, UpdateAdvertisementRequestDto requestDto) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_ADVERTISEMENT));
        if(!advertisement.getMember().getId().equals(member.getId())){
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        advertisement.retryBid(requestDto.getBidPrice());
        return new UpdateAdvertisementResponseDto(advertisement.getId());
    }


}
