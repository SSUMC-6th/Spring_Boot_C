package com.example.umc.web.dto.reponse;

import com.example.umc.domain.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponseDto {
    private Long id;
    private String name;
    private String address;
    private Float score;
    private Long regionId;


    @Builder
    public StoreResponseDto(Long id, String name, String address, Float score, Long regionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.score = score;
        this.regionId = regionId;
    }

    public static StoreResponseDto fromEntity(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .score(store.getScore())
                .regionId(store.getRegion().getId())
                .build();
    }
}
