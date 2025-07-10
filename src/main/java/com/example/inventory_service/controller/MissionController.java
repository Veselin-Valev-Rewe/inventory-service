package com.example.inventory_service.controller;

import com.example.inventory_service.dto.mission.CreateMissionDto;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;
import com.example.inventory_service.service.MissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController {
    private final MissionService missionService;

    @PostMapping
    public ResponseEntity<MissionDto> createMission(@RequestBody @Valid CreateMissionDto MissionDto) {
        var mission = missionService.createMission(MissionDto);
        URI location = URI.create("/api/missions/" + mission.getId());
        return ResponseEntity.created(location).body(mission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionDto> updateMission(@PathVariable @Positive int id, @RequestBody @Valid UpdateMissionDto MissionDto) {
        var mission = missionService.updateMission(id, MissionDto);
        return ResponseEntity.ok(mission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable @Positive int id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionDto> completeMission(@PathVariable @Positive int id) {
        var mission = missionService.completeMission(id);
        return ResponseEntity.ok(mission);
    }
}
