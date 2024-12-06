package com.example.elevendash.domain.advertisement.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindAllAdvertisementResponseDto {
    private final List<AdvertisementInfo> advertisementInfoList;
    @Getter
    public static class AdvertisementInfo{
        private final Long advertisementId;
        private final Long storeId;
        private final Long memberId;
        private final Integer bidPrice;

        public AdvertisementInfo(Long advertisementId, Long storeId, Long memberId, Integer bidPrice) {
            this.advertisementId = advertisementId;
            this.storeId = storeId;
            this.memberId = memberId;
            this.bidPrice = bidPrice;
        }
    }
}
