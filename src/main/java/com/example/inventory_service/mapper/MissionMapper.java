package com.example.inventory_service.mapper;

import com.example.inventory_service.data.entity.Mission;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MissionMapper {
    MissionDto toMissionDto(Mission mission);

    void updateMissionFromDto(UpdateMissionDto dto, @MappingTarget Mission mission);
}