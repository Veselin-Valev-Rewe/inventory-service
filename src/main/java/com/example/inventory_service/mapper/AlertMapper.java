package com.example.inventory_service.mapper;

import com.example.inventory_service.data.entity.Alert;
import com.example.inventory_service.data.enums.AlertStatus;
import com.example.inventory_service.dto.alert.AlertDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlertMapper {
    @Mapping(target = "alertName", expression = "java(getMapValue(dto.getLabels(), \"alertname\"))")
    @Mapping(target = "severity", expression = "java(getMapValue(dto.getLabels(), \"severity\"))")
    @Mapping(target = "instance", expression = "java(getMapValue(dto.getLabels(), \"instance\"))")
    @Mapping(target = "summary", expression = "java(getMapValue(dto.getAnnotations(), \"summary\"))")
    @Mapping(target = "description", expression = "java(getMapValue(dto.getAnnotations(), \"description\"))")
    @Mapping(target = "status", expression = "java(mapStatus(dto.getStatus()))")
    @Mapping(target = "fingerprint", expression = "java(generateFingerprint(dto))")
    @Mapping(target = "generatorUrl", source = "dto.generatorURL")
    @Mapping(target = "startsAt", expression = "java(mapToLocalDateTime(dto.getStartsAt()))")
    @Mapping(target = "endsAt", expression = "java(mapToLocalDateTime(dto.getEndsAt()))")
    Alert toAlert(AlertDto dto);

    @Mapping(target = "alertName", expression = "java(getMapValue(dto.getLabels(), \"alertname\"))")
    @Mapping(target = "severity", expression = "java(getMapValue(dto.getLabels(), \"severity\"))")
    @Mapping(target = "instance", expression = "java(getMapValue(dto.getLabels(), \"instance\"))")
    @Mapping(target = "summary", expression = "java(getMapValue(dto.getAnnotations(), \"summary\"))")
    @Mapping(target = "description", expression = "java(getMapValue(dto.getAnnotations(), \"description\"))")
    @Mapping(target = "status", expression = "java(mapStatus(dto.getStatus()))")
    @Mapping(target = "fingerprint", expression = "java(generateFingerprint(dto))")
    @Mapping(target = "generatorUrl", source = "dto.generatorURL")
    @Mapping(target = "startsAt", expression = "java(mapToLocalDateTime(dto.getStartsAt()))")
    @Mapping(target = "endsAt", expression = "java(mapToLocalDateTime(dto.getEndsAt()))")
    void updateAlert(AlertDto dto, @MappingTarget Alert alert);

    default String getMapValue(Map<String, String> map, String key) {
        return map != null ? map.getOrDefault(key, null) : null;
    }

    default AlertStatus mapStatus(String status) {
        if (status == null) return null;
        try {
            return AlertStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default String generateFingerprint(AlertDto dto) {
        String alertName = dto.getLabels() != null ? dto.getLabels().getOrDefault("alertname", "") : "";
        String instance = dto.getLabels() != null ? dto.getLabels().getOrDefault("instance", "") : "";
        String startsAt = dto.getStartsAt() != null ? dto.getStartsAt() : "";

        return String.join("--", alertName, instance, startsAt).toLowerCase();
    }


    default LocalDateTime mapToLocalDateTime(String dateStr) {
        if (dateStr == null) return null;
        return OffsetDateTime.parse(dateStr).toLocalDateTime();
    }

}
