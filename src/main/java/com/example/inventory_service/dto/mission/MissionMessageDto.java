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
public class MissionMessageDto {
    private int id;
    
    private int finalCount;

    private int originalCount;

    private OperationType operationType;

    private int userId;

    private int productId;

    private int warehouseId;
}
