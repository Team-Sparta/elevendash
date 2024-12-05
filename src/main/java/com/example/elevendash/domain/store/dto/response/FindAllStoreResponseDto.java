package com.example.elevendash.domain.store.dto.response;

import com.example.elevendash.domain.store.dto.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindAllStoreResponseDto {
    private final List<StoreInfo> advertisementStoreInfoList;
    private final List<StoreInfo> storeInfoList;
    private final PageInfo pageInfo;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
    }
}
