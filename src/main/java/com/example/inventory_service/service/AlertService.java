package com.example.inventory_service.service;

import com.example.inventory_service.dto.alert.AlertManagerPayload;

public interface AlertService {
    void handleAlert(AlertManagerPayload payload);
}
