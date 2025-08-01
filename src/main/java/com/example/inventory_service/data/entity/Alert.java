package com.example.inventory_service.data.entity;

import com.example.inventory_service.data.enums.AlertStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Alert extends BaseEntity {
    private String alertName;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    private String severity;

    private String instance;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private String summary;

    private String description;

    private String fingerprint;

    private String generatorUrl;
}
