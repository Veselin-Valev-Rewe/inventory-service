package com.example.inventory_service.dto.mission;

import com.example.inventory_service.data.enums.MissionStatus;
import com.example.inventory_service.data.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMissionDto {
    private int id;

    private MissionStatus status;

    private OperationType operationType;
}
