package com.example.umc.web.dto.request;

import com.example.umc.domain.Member;
import com.example.umc.domain.Review;
import com.example.umc.domain.Store;
import com.example.umc.validation.annotation.ExistStore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewRequest {

    @NotNull(message = "리뷰 제목은 필수입니다.")
    private String title;

    @NotNull(message = "리뷰 평점은 필수입니다.")
    private Float score;

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @ExistStore
    @NotNull(message = "가게 ID는 필수입니다.")
    private Long storeId;

    @Builder
    public CreateReviewRequest(String title, Float score, Long memberId, Long storeId) {
        this.title = title;
        this.score = score;
        this.memberId = memberId;
        this.storeId = storeId;
    }

    public static Review toEntity(CreateReviewRequest request, Member member, Store store) {
        return Review.builder()
                .title(request.getTitle())
                .score(request.getScore())
                .member(member)
                .store(store)
                .build();
    }

}
