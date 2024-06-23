package com.example.umc.web.dto.request;

import com.example.umc.domain.Store;
import com.example.umc.validation.annotation.ExistRegion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStoreRequest {

    @Schema(description = "가게 이름", example = "맛집")
    @NotNull(message = "가게 이름은 필수입니다.")
    private String name;

    @Schema(description = "가게 주소", example = "서울시 강남구")
    @NotNull(message = "가게 주소는 필수입니다.")
    private String address;

    @NotNull(message = "가게 평점은 필수입니다.")
    private Float score;

    @ExistRegion
    @NotNull(message = "지역 ID는 필수입니다.")
    private Long regionId;

    @Builder
    public CreateStoreRequest(String name, String address, Float score, Long regionId) {
        this.name = name;
        this.address = address;
        this.score = score;
        this.regionId = regionId;
    }

    public static Store toEntity(CreateStoreRequest request) {
        return Store.builder()
                .name(request.getName())
                .address(request.getAddress())
                .score(request.getScore())
                .build();
    }
}
