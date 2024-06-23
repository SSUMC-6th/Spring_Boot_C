package com.example.umc.service;

import com.example.umc.domain.Member;
import com.example.umc.domain.Review;
import com.example.umc.domain.Store;
import com.example.umc.repository.MemberRepository;
import com.example.umc.repository.ReviewRepository;
import com.example.umc.repository.StoreRepository;
import com.example.umc.web.dto.reponse.ReviewResponseDto;
import com.example.umc.web.dto.request.CreateReviewRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    public ReviewResponseDto addReview(CreateReviewRequest createReviewRequest) {
        Member member = memberRepository.findById(createReviewRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

       /* Store store = storeRepository.findById(createReviewRequest.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
            이렇게 처리를 할 필요가 없는 거지 이미 유효성 검사를 했기 때문에
        */
        Store store = storeRepository.getReferenceById(createReviewRequest.getStoreId());
        Review review = CreateReviewRequest.toEntity(createReviewRequest, member, store);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.fromEntity(savedReview);
    }
}
