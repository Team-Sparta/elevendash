package com.example.elevendash.domain.advertisement.service;

import com.example.elevendash.domain.advertisement.dto.request.AddAdvertisementRequestDto;
import com.example.elevendash.domain.advertisement.dto.response.AddAdvertisementResponseDto;
import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.advertisement.repository.AdvertisementRepository;
import com.example.elevendash.domain.member.entity.Member;
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
        Store advertieseStore = storeRepository.findByIdAndIsDeletedAndMember(requestDto.getStoreId(),Boolean.FALSE,member)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_STORE));
        // database에서 중복되는 광고를 방지하는데 여기서 예외처리할지 고민
        Advertisement advertisement = new Advertisement(requestDto.getBidPrice(),member,advertieseStore);
        advertisementRepository.save(advertisement);
        return new AddAdvertisementResponseDto(advertisement.getId());
    }


}
