package com.example.umc.repository;

import com.example.umc.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //특정 사용자가 작성한 리뷰를 페이징 처리하여 가져오는 메서드 추가
    Page<Review> findByMemberId(Long memberId, Pageable pageable);
}
