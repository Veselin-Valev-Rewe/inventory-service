package com.example.inventory_service.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {
    private String status;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private String startsAt;
    private String endsAt;
    private String generatorURL;
}
