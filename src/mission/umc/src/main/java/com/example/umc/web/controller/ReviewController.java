package com.example.umc.web.controller;


import com.example.umc.common.BaseResponse;
import com.example.umc.service.ReviewService;
import com.example.umc.web.dto.reponse.ReviewResponseDto;
import com.example.umc.web.dto.request.CreateReviewRequest;
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
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "review 컨트롤러", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 생성",
            responses = @ApiResponse(responseCode = "200", description = "리뷰를 생성합니다")
    )
    @PostMapping
    public BaseResponse<ReviewResponseDto> addReview(@RequestBody @Valid CreateReviewRequest createReviewRequest) {

        ReviewResponseDto createdReviewDto = reviewService.addReview(createReviewRequest);
        return BaseResponse.onSuccess(createdReviewDto);
    }
}
