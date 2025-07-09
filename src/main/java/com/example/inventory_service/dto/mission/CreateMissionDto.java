package com.example.inventory_service.dto.mission;

import com.example.inventory_service.data.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMissionDto {
    private OperationType operationType;

    private int operationalCount;

    private int userId;

    private int productId;

    private int warehouseId;
}
