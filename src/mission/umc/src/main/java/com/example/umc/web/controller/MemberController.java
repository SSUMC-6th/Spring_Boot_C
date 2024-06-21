package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import com.example.umc.service.MemberService;
import com.example.umc.validation.annotation.AlreadyExistMission;
import com.example.umc.validation.annotation.ExistMission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "member 컨트롤러", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "가게 미션을 도전중인 미션에 추가")
    @PostMapping("/add/{missionId}/{memberId}")
    public BaseResponse<String> addMission(@PathVariable @AlreadyExistMission Long missionId, @PathVariable Long memberId){

        memberService.addMission(missionId, memberId);
        return BaseResponse.onSuccess("미션 생성 완료");

    }
}
