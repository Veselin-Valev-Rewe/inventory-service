package com.example.inventory_service.service.impl;

import com.example.inventory_service.data.repository.AlertRepository;
import com.example.inventory_service.dto.alert.AlertDto;
import com.example.inventory_service.dto.alert.AlertManagerPayload;
import com.example.inventory_service.mapper.AlertMapper;
import com.example.inventory_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    @Override
    public void handleAlert(AlertManagerPayload payload) {
        for (AlertDto alert : payload.getAlerts()) {
            var fingerprint = alertMapper.generateFingerprint(alert);

            if (alertRepository.existsByFingerprint(fingerprint)) {
                var existingAlert = alertRepository.getByFingerprint(fingerprint);
                alertMapper.updateAlert(alert, existingAlert);
                alertRepository.save(existingAlert);
            } else {
                var newAlert = alertMapper.toAlert(alert);
                alertRepository.save(newAlert);
            }
        }
    }
}
