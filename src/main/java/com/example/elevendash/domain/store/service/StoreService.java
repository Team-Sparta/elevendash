package com.example.elevendash.domain.store.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.dto.request.RegisterStoreRequestDto;
import com.example.elevendash.domain.store.dto.response.RegisterStoreResponseDto;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    /**
     * 음식점 등록 메소드
     * @param member 등록하려는 유저
     * @param multipartFile 음식점 이미지 파일
     * @param dto 음식점에 필요한 정보
     * @return
     */
    @Transactional
    public RegisterStoreResponseDto registerStore(Member member, MultipartFile multipartFile, RegisterStoreRequestDto dto) {
        // 오픈 마감시간 검증
        if(isValidBusinessHours(dto.getOpenTime(),dto.getCloseTime())){
            throw new BaseException(ErrorCode.VALIDATION_ERROR);
        }
        // 스토어 수 검증
        if(isValidStoreNumber(member, 3L)) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR);
        }
        String storeImage = convert(multipartFile);
        Store savedStore = Store.builder()
                .storeName(dto.getStoreName())
                .storeDescription(dto.getStoreDescription())
                .storeAddress(dto.getStoreAddress())
                .storePhone(dto.getStorePhone())
                .leastAmount(dto.getLeastAmount())
                .member(member)
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .build();
        storeRepository.save(savedStore);
        return new RegisterStoreResponseDto(savedStore.getId());
    }

    /**
     * 음식점 수 검증 메소드 (3개인 경우에 추가할경우 위반)
     * @param member
     * @param LimitNumber
     * @return
     */
    private Boolean isValidStoreNumber(Member member,Long LimitNumber) {
        Long storeNumber = storeRepository.countByMemberAndIsDeleted(member, Boolean.FALSE);
        return storeNumber.equals(LimitNumber);
    }
    /**
     * 오픈 마감시간 검증 메소드
     * @param openTime
     * @param closeTime
     * @return
     */
    private Boolean isValidBusinessHours(LocalTime openTime, LocalTime closeTime) {
        return openTime.isBefore(closeTime);
    }
    /**
     * 임시 파일 변환 메소드
     * @param multipartFile
     * @return
     */
    private String convert (MultipartFile multipartFile) {
        return "storePictureExample.jpg";
    }
}
