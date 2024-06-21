package com.example.umc.service;

import com.example.umc.domain.Mission;
import com.example.umc.domain.Store;
import com.example.umc.repository.MissionRepository;
import com.example.umc.repository.StoreRepository;
import com.example.umc.web.dto.request.CreateMissionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final StoreRepository storeRepository;
    private final MissionRepository missionRepository;

    public void addMission(CreateMissionRequest createMissionRequest) {

        Store store = storeRepository.getReferenceById(createMissionRequest.getStoreId());
        missionRepository.save(CreateMissionRequest.toEntity(createMissionRequest, store));

        return;
    }

}
