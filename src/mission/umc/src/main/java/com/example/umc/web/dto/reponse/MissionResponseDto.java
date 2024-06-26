package com.example.umc.web.dto.reponse;

import com.example.umc.domain.Mission;
import com.example.umc.domain.maaping.MemberMission;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Getter
@Builder
public class MissionResponseDto {

    private Long id;
    private Integer reward;
    private LocalDate deadline;

    @Builder
    public MissionResponseDto (Long id, Integer reward, LocalDate deadline){
        this.id = id;
        this.reward = reward;
        this.deadline = deadline;
    }

    public static MissionResponseDto fromEntity(Mission mission){
        return MissionResponseDto.builder()
                .id(mission.getId())
                .reward(mission.getReward())
                .deadline(mission.getDeadline())
                .build();
    }

    public static MissionResponseDto fromMemberMissionEntity(MemberMission memberMission) {
        return MissionResponseDto.builder()
                .id(memberMission.getMission().getId())
                .reward(memberMission.getMission().getReward())
                .deadline(memberMission.getMission().getDeadline())
                .build();
    }


}
