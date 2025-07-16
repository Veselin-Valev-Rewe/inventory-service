package com.example.inventory_service.mapper;

import com.example.inventory_service.data.entity.Mission;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.MissionMessageDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MissionMapper {
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    MissionDto toMissionDto(Mission mission);

    @Mapping(target = "id", ignore = true)
    void updateMissionFromDto(UpdateMissionDto dto, @MappingTarget Mission mission);

    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    MissionMessageDto toMissionMessage(Mission mission);
}