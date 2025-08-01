package com.example.inventory_service.data.repository;

import com.example.inventory_service.data.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
    boolean existsByFingerprint(String fingerprint);

    Alert getByFingerprint(String fingerprint);
}
