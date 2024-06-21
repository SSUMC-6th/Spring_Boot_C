package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import com.example.umc.service.MissionService;
import com.example.umc.web.dto.request.CreateMissionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mission")
@RequiredArgsConstructor
@Tag(name = "mission 컨트롤러", description = "미션 관련 API")
public class MissionController {

    private final MissionService missionService;

    @Operation(
            summary = "미션 생성",
            description = "미션을 생성합니다"
    )
    @PostMapping("/add")
    public BaseResponse<String> addMission(@RequestBody @Valid CreateMissionRequest createMissionRequest){
        missionService.addMission(createMissionRequest);
        return BaseResponse.onSuccess("미션 생성 완료");
    }
}
