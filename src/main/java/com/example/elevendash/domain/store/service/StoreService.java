package com.example.elevendash.domain.store.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.store.dto.request.RegisterStoreRequestDto;
import com.example.elevendash.domain.store.dto.response.RegisterStoreResponseDto;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    @Transactional
    public RegisterStoreResponseDto registerStore(Member member, MultipartFile multipartFile, RegisterStoreRequestDto dto) {
        String storeImage = convert(multipartFile);
        Store savedStore = Store.builder()
                .storeName(dto.getStoreName())
                .storeDescription(dto.getStoreDescription())
                .storeAddress(dto.getStoreAddress())
                .storePhone(dto.getStorePhone())
                .leastAmount(dto.getLeastAmount())
                .member(member)
                .build();
        storeRepository.save(savedStore);
        return new RegisterStoreResponseDto(savedStore.getId());
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
