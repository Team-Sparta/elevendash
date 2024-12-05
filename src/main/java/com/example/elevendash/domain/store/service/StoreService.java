package com.example.elevendash.domain.store.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.menu.dto.CategoryInfo;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.menu.repository.CategoryRepository;
import com.example.elevendash.domain.menu.repository.MenuRepository;
import com.example.elevendash.domain.store.dto.StoreInfo;
import com.example.elevendash.domain.store.dto.request.RegisterStoreRequestDto;
import com.example.elevendash.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.elevendash.domain.store.dto.response.*;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.enums.SortMode;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.s3.S3Service;
import com.example.elevendash.global.s3.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final S3Service s3Service;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;

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
        String storeImage = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            storeImage = convert(multipartFile);
        }
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
        if(!member.getRole().equals(MemberRole.OWNER)){
            throw new BaseException(ErrorCode.NOT_OWNER);
        }
        // soft delete
        deleteStore.delete();
        return new DeleteStoreResponseDto(deleteStore.getId());
    }

    /**
     * 가게 수정 서비스 메소드
     * @param member
     * @param storeId
     * @param multipartFile
     * @param dto
     * @return
     */
    @Transactional
    public UpdateStoreResponseDto updateStore(Member member, Long storeId,MultipartFile multipartFile, UpdateStoreRequestDto dto) {
        // 오픈 마감시간 검증
        if(!isValidBusinessHours(dto.getOpenTime(),dto.getCloseTime())){
            throw new BaseException(ErrorCode.NOT_VALID_OPEN_TIME);
        }
        // OWNER 권한 검증
        if(!member.getRole().equals(MemberRole.OWNER)){
            throw new BaseException(ErrorCode.NOT_OWNER);
        }
        String storeImage = null;
        // 삭제된 상점 제외 조회
        Store updateStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        // 상점 소유자 검증
        if (!updateStore.getMember().equals(member)){
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            storeImage = convert(multipartFile);
        }
        updateStore.update(dto.getStoreName(),dto.getStoreDescription(),dto.getStoreAddress()
                ,dto.getStorePhone(),dto.getLeastAmount(),storeImage,dto.getOpenTime(),dto.getCloseTime());
        return new UpdateStoreResponseDto(updateStore.getId());
    }

    /**
     * 상점 단건 조회 서비스 메소드
     * @param storeId
     * @return
     */
    @Transactional
    public FindStoreResponseDto findStore(Long storeId) {
        // 삭제된건 제외하고 조회
        Store findStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        List<Object[]> categories = categoryRepository.findAllCategoryInfo(findStore);
        // 카테고리별로 메뉴 연결을 위한 map
        Map<String,List<CategoryInfo.MenuInfoForCategory>> menuMap = new HashMap<>();
        //연결 로직
        for(Object[] category : categories){
            String categoryName = ((Categories)category[0]).getKoreanName();
            String menuName = (String) category[1];
            String menuDescription = (String) category[2];
            Integer menuPrice = (Integer) category[3];
            String menuImage = (String) category[4];
            Long menuId = (Long) category[5];

            CategoryInfo.MenuInfoForCategory menuInfo = new CategoryInfo.MenuInfoForCategory(menuName,menuDescription,menuPrice,menuImage,menuId);
            menuMap.putIfAbsent(categoryName, new ArrayList<>());
            menuMap.get(categoryName).add(menuInfo);
        }
        // map을 객체로 변환
        List<CategoryInfo> categoryList = new ArrayList<>();
        for(Map.Entry<String,List<CategoryInfo.MenuInfoForCategory>> entry : menuMap.entrySet()){
            categoryList.add(new CategoryInfo(entry.getKey(),entry.getValue()));
        }

        return new FindStoreResponseDto(findStore.getId(),findStore.getStoreName()
                ,findStore.getStoreDescription(),findStore.getOpenTime(),findStore.getCloseTime()
                ,findStore.getStoreAddress(),findStore.getStorePhone(),findStore.getLeastAmount(),findStore.getStoreImage(),categoryList);
    }

    // 시간이 되면 동적 쿼리로 작성
    @Transactional
    public FindAllStoreResponseDto findAllStore(Member loginMember,Categories category,Integer pageSize, Integer pageNumber, SortMode sortMode) {
        Page<StoreInfo> storeInfoList = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if(sortMode.equals(SortMode.NORMAL)){
            storeInfoList = storeRepository.findAllByIsDeleted(Boolean.FALSE,pageable).map(store -> new StoreInfo(
                    store.getId(),store.getBookmarks().size(),store.getStoreName(),store.getOpenTime()
                    ,store.getCloseTime(),store.getStoreAddress(),store.getLeastAmount()
            ));
        }
        if(sortMode.equals(SortMode.BOOKMARK_RANKING)){
            storeInfoList = storeRepository.findAllByIsDeletedBookmarkSort(Boolean.FALSE,pageable).map(store -> new StoreInfo(
                    store.getId(),store.getBookmarks().size(),store.getStoreName(),store.getOpenTime()
                    ,store.getCloseTime(),store.getStoreAddress(),store.getLeastAmount()
            ));
        }
        if(sortMode.equals(SortMode.MY_BOOKMARK)){
            storeInfoList = storeRepository.findAllByIsDeletedAndMyBookmark(Boolean.FALSE,loginMember,pageable).map(store -> new StoreInfo(
                    store.getId(),store.getBookmarks().size(),store.getStoreName(),store.getOpenTime()
                    ,store.getCloseTime(),store.getStoreAddress(),store.getLeastAmount()
            ));
        }
        if(sortMode.equals(SortMode.CATEGORY)){
            storeInfoList = storeRepository.findAllByIsDeletedAndCategory(Boolean.FALSE,category,pageable).map(store -> new StoreInfo(
                    store.getId(),store.getBookmarks().size(),store.getStoreName(),store.getOpenTime()
                    ,store.getCloseTime(),store.getStoreAddress(),store.getLeastAmount()
            ));
        }


        if(storeInfoList == null){
            throw new BaseException(ErrorCode.NOT_FOUND_STORE);
        }

        return new FindAllStoreResponseDto(storeInfoList.getContent(),
                new FindAllStoreResponseDto.PageInfo(storeInfoList.getNumber()+1,
                        storeInfoList.getSize(),
                        storeInfoList.getTotalElements(),
                        storeInfoList.getTotalPages()));
    }




    /**
     * 음식점 수 검증 메소드 (3개 미만 경우에 가능)
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

    private String convert (MultipartFile image) {
        String imageUrl = null;
        if (image != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadStoreImage(image);
            imageUrl = uploadImageInfo.ImageUrl();
        }
        return imageUrl;
    }
}
