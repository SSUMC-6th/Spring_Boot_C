package com.example.umc.repository;

import com.example.umc.domain.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Page<Mission> findByStoreId(Long storeId, Pageable pageable);

}
