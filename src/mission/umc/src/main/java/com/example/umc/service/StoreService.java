package com.example.umc.service;

import com.example.umc.domain.Region;
import com.example.umc.domain.Store;
import com.example.umc.repository.RegionRepository;
import com.example.umc.repository.StoreRepository;
import com.example.umc.web.dto.reponse.StoreResponseDto;
import com.example.umc.web.dto.request.CreateStoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final RegionRepository regionRepository;

    public StoreResponseDto addStoreToRegion(CreateStoreRequest createStoreRequest) {
        Region region = regionRepository.findById(createStoreRequest.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));

        Store store = CreateStoreRequest.toEntity(createStoreRequest);
        store.setRegion(region);
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.fromEntity(savedStore);
    }

}
