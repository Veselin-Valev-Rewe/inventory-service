package com.example.inventory_service.controller;

import com.example.inventory_service.dto.alert.AlertManagerPayload;
import com.example.inventory_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {
    public final AlertService alertService;

    @PostMapping
    public ResponseEntity<Void> receiveAlert(@RequestBody AlertManagerPayload payload) {
        alertService.handleAlert(payload);
        return ResponseEntity.noContent().build();
    }
}
