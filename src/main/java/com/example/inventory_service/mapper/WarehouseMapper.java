package com.example.inventory_service.mapper;

import com.example.inventory_service.data.entity.Warehouse;
import com.example.inventory_service.dto.warehouse.WarehouseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    @Mapping(target = "id", ignore = true)
    void updateWarehouseFromDto(WarehouseDto dto, @MappingTarget Warehouse warehouse);

    Warehouse toWarehouse(WarehouseDto warehouseDto);
}