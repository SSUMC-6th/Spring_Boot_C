package com.example.umc.web.dto.reponse;

import com.example.umc.domain.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {
    private Long id;
    private String title;
    private Float score;
    private Long memberId;
    private Long storeId;

    public static ReviewResponseDto fromEntity(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .score(review.getScore())
                .memberId(review.getMember().getId())
                .storeId(review.getStore().getId())
                .build();
    }
}
