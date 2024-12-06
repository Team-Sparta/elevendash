package com.example.elevendash.domain.advertisement.dto.response;

import com.example.elevendash.domain.advertisement.entity.Advertisement;
import com.example.elevendash.domain.advertisement.enums.AdvertisementStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindAllMyAdvertisementResponseDto {
    private final List<AdvertisementInfoForOwner> advertisementInfoForOwner;
    @Getter
    public static class AdvertisementInfoForOwner {
        private final Long advertisementId;
        private final Long storeId;
        private final String rejectReason;
        private final Integer bidPrice;
        private final AdvertisementStatus status;

        public AdvertisementInfoForOwner(Long advertisementId, Long storeId, String rejectReason, Integer bidPrice, AdvertisementStatus status) {
            this.advertisementId = advertisementId;
            this.storeId = storeId;
            this.rejectReason = rejectReason;
            this.bidPrice = bidPrice;
            this.status = status;

        }
    }
}
