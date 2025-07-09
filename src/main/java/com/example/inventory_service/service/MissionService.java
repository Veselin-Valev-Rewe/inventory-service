package com.example.inventory_service.service;

import com.example.inventory_service.dto.mission.CreateMissionDto;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;
import jakarta.validation.Valid;

public interface MissionService {
    MissionDto createMission(@Valid CreateMissionDto missionDto);

    MissionDto updateMission(@Valid UpdateMissionDto missionDto);

    void deleteMission(int id);

    MissionDto completeMission(int id);
}
