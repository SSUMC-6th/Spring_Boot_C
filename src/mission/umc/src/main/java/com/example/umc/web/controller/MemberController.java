package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import com.example.umc.service.MemberService;
import com.example.umc.service.ReviewService;
import com.example.umc.validation.annotation.AlreadyExistMission;
import com.example.umc.validation.annotation.ExistMission;
import com.example.umc.web.dto.reponse.MissionResponseDto;
import com.example.umc.web.dto.reponse.ReviewResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "member 컨트롤러", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final ReviewService reviewService;

    @Operation(summary = "가게 미션을 도전중인 미션에 추가")
    @PostMapping("/add/{missionId}/{memberId}")
    public BaseResponse<String> addMission(@PathVariable @AlreadyExistMission Long missionId, @PathVariable Long memberId){

        memberService.addMission(missionId, memberId);
        return BaseResponse.onSuccess("미션 생성 완료");

    }

    @Operation(summary = "회원의 리뷰 조회")
    @GetMapping("/get/review/{memberId}")
    public BaseResponse<Page<ReviewResponseDto>> getReview(@PathVariable Long memberId, Pageable pageable){
        return BaseResponse.onSuccess(reviewService.getReviewsByMemberId(memberId, pageable));
    }

    @GetMapping("/get/missions/{memberId}")
    public BaseResponse<Page<MissionResponseDto>> getMissionsByMemberId(@PathVariable Long memberId, Pageable pageable){
        return BaseResponse.onSuccess(memberService.getMissionsByMemberId(memberId, pageable));
    }

}
