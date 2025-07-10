package com.example.inventory_service.service;

import com.example.inventory_service.dto.mission.CreateMissionDto;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;

public interface MissionService {
    MissionDto createMission(CreateMissionDto missionDto);

    MissionDto updateMission(int id, UpdateMissionDto missionDto);

    void deleteMission(int id);

    void completeMission(int id);
}
