package com.example.umc.web.dto.request;

import com.example.umc.domain.Mission;
import com.example.umc.domain.Store;
import com.example.umc.repository.StoreRepository;
import com.example.umc.validation.annotation.ExistStore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Builder
@Getter
public class CreateMissionRequest {

        @NotNull(message = "미션 점수는 필수입니다.")
        private Integer reward;

        @NotNull(message = "마감 기한은 필수입니다.")
        @Schema(description = "시작 날짜 (형식: yyyy-MM-dd)", example = "2024-07-31")
        private String deadline;

        @NotNull(message = "미션 설명은 필수입니다.")
        private String missionSpec;

        @ExistStore
        @Schema(description = "가게 ID", example = "1")
        @NotNull(message = "가게 ID는 필수입니다.")
        private Long storeId;

        @Builder
        public CreateMissionRequest(Integer reward, String deadline, String missionSpec, Long storeId) {
            this.reward = reward;
            this.deadline = deadline;
            this.missionSpec = missionSpec;
            this.storeId = storeId;
        }

        public static Mission toEntity(CreateMissionRequest request, Store store) {
            return Mission.builder()
                    .reward(request.getReward())
                    .deadline(LocalDate.parse(request.getDeadline()))
                    .missionSpec(request.getMissionSpec())
                    .store(store)
                    .build();
        }

}
