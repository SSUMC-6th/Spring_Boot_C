package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import com.example.umc.service.StoreService;
import com.example.umc.web.dto.reponse.MissionResponseDto;
import com.example.umc.web.dto.reponse.StoreResponseDto;
import com.example.umc.web.dto.request.CreateStoreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Tag(name = "Store 컨트롤러", description = "가게 관련 API")
public class StoreController {
    private final StoreService storeService;


    @Operation(
            summary = "가게 생성",
            responses = @ApiResponse(responseCode = "200", description = "가게를 생성합니다")
    )
    @PostMapping("/region")
    public BaseResponse<StoreResponseDto> addStoreToRegion(@Valid @RequestBody CreateStoreRequest createStoreRequest) {

        StoreResponseDto createdStoreDto = storeService.addStoreToRegion(createStoreRequest);
        return BaseResponse.onSuccess(createdStoreDto);
    }

    @Operation(
            summary = "가게 미션 조회",
            responses = @ApiResponse(responseCode = "200", description = "가게의 미션을 조회합니다")
    )
    @GetMapping("/get/missions/{storeId}")
    public BaseResponse<Page<MissionResponseDto>> getMissionsByStoreId(@PathVariable Long storeId, Pageable pageable){
        return BaseResponse.onSuccess(storeService.getMissionsByStoreId(storeId, pageable));
    }

}
