package com.example.elevendash.domain.store.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.store.dto.request.RegisterStoreRequestDto;
import com.example.elevendash.domain.store.dto.response.DeleteStoreResponseDto;
import com.example.elevendash.domain.store.dto.response.RegisterStoreResponseDto;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.s3.S3Service;
import com.example.elevendash.global.s3.UploadImageInfo;
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
    private final S3Service s3Service;

    /**
     * 음식점 등록 메소드
     * 예외사항: 오픈 마감시간 검증 스토어 수 검증
     * @param member 등록하려는 유저
     * @param multipartFile 음식점 이미지 파일
     * @param dto 음식점에 필요한 정보
     * @return
     */
    @Transactional
    public RegisterStoreResponseDto registerStore(Member member, MultipartFile multipartFile, RegisterStoreRequestDto dto) {
        // 오픈 마감시간 검증
        if(!isValidBusinessHours(dto.getOpenTime(),dto.getCloseTime())){
            throw new BaseException(ErrorCode.NOT_VALID_OPEN_TIME);
        }
        // 스토어 수 검증
        if(!isValidStoreNumber(member, 3L)) {
            throw new BaseException(ErrorCode.ENOUGH_STORE);
        }
        // OWNER 권한 검증
        if(!member.getRole().equals(MemberRole.OWNER)){
            throw new BaseException(ErrorCode.NOT_OWNER);
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
                .storeImage(storeImage)
                .build();
        storeRepository.save(savedStore);
        return new RegisterStoreResponseDto(savedStore.getId());
    }

    /**
     * 상점 삭제 메소드
     * 예외사항: 상점이 존재하지 않는경우, 상점의 멤버와 삭제하려는 멤버가 일치하지 않는경우
     * @param member
     * @param storeId
     * @return
     */
    @Transactional
    public DeleteStoreResponseDto deleteStore(Member member, Long storeId) {
        // 상점을 찾지 못한경우 예외
        Store deleteStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));

        // 상점의 멤버와 현재 멤버가 일치하지 않은경우 예외
        if(!deleteStore.getMember().equals(member)){
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        // soft delete
        deleteStore.delete();
        return new DeleteStoreResponseDto(deleteStore.getId());
    }

    /**
     * 음식점 수 검증 메소드 (3개인 경우에 추가할경우 위반)
     * @param member
     * @param LimitNumber
     * @return
     */
    public Boolean isValidStoreNumber(Member member,Long LimitNumber) {
        Long storeNumber = storeRepository.countByMemberAndIsDeleted(member, Boolean.FALSE);
        return storeNumber <(LimitNumber);
    }
    /**
     * 오픈 마감시간 검증 메소드
     * @param openTime
     * @param closeTime
     * @return
     */
    public Boolean isValidBusinessHours(LocalTime openTime, LocalTime closeTime) {
        return openTime.isBefore(closeTime);
    }

    public String convert (MultipartFile image) {
        String imageUrl = null;
        if (image != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(image);
            imageUrl = uploadImageInfo.ImageUrl();
        }
        return imageUrl;
    }
}
