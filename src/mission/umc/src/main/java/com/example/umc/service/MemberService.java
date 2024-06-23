package com.example.umc.service;

import com.example.umc.domain.Member;
import com.example.umc.domain.Mission;
import com.example.umc.domain.enums.MissionStatus;
import com.example.umc.domain.maaping.MemberMission;
import com.example.umc.repository.MemberMissionRepository;
import com.example.umc.repository.MemberRepository;
import com.example.umc.repository.MissionRepository;
import com.example.umc.validation.annotation.ExistMission;
import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final MemberMissionRepository memberMissionRepository;
    
    public void addMission(Long missionId, Long memberId) {
        Mission mission = missionRepository.getReferenceById(missionId);
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new IllegalArgumentException("Member not found"));

        MemberMission memberMission = MemberMission.builder()
                .status(MissionStatus.CHALLENGING)
                .member(member)
                .mission(mission)
                .build();

        member.getMemberMissionList().add(memberMission);
        memberMissionRepository.save(memberMission);
    }
}
