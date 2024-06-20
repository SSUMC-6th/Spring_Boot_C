package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import com.example.umc.service.StoreService;
import com.example.umc.web.dto.reponse.StoreResponseDto;
import com.example.umc.web.dto.request.CreateStoreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
