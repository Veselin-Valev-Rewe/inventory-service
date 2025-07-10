package com.example.inventory_service.service.impl;

import com.example.inventory_service.data.entity.*;
import com.example.inventory_service.data.enums.MissionStatus;
import com.example.inventory_service.data.repository.*;
import com.example.inventory_service.dto.mission.CreateMissionDto;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;
import com.example.inventory_service.mapper.MissionMapper;
import com.example.inventory_service.service.MissionService;
import com.example.inventory_service.util.errormessage.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final MissionMapper missionMapper;

    @Override
    public MissionDto createMission(CreateMissionDto missionDto) {
        var user = getUser(missionDto.getUserId());
        var warehouse = getWarehouse(missionDto.getWarehouseId());
        var product = getProduct(missionDto.getProductId());
        var inventory = getInventory(product.getId(), warehouse.getId());

        var mission = Mission.builder()
                .operationType(missionDto.getOperationType())
                .status(MissionStatus.IN_PROGRESS)
                .user(user)
                .warehouse(warehouse)
                .product(product)
                .build();

        var operationType = missionDto.getOperationType();
        int originalCount;
        int finalCount;

        switch (operationType) {
            case INITIAL_PLACEMENT -> {
                originalCount = 0;
                finalCount = missionDto.getOperationalCount();
            }
            case UPDATE -> {
                originalCount = inventory.getCount();
                finalCount = originalCount + missionDto.getOperationalCount();
            }
            case UNSTOW -> {
                originalCount = inventory.getCount();
                finalCount = 0;
            }
            default ->
                    throw new IllegalArgumentException(String.format(ErrorMessages.UNSUPPORTED_OPERATION_TYPE, operationType));
        }

        mission.setOriginalCount(originalCount);
        mission.setFinalCount(finalCount);
        var savedMission = missionRepository.save(mission);

        return missionMapper.toMissionDto(savedMission);
    }

    @Override
    public MissionDto updateMission(int id, UpdateMissionDto missionDto) {
        var mission = getMission(id);
        missionMapper.updateMissionFromDto(missionDto, mission);
        var savedMission = missionRepository.save(mission);

        return missionMapper.toMissionDto(savedMission);
    }

    @Override
    public void deleteMission(int id) {
        if (!missionRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(ErrorMessages.MISSION_NOT_FOUND, id));
        }

        missionRepository.deleteById(id);
    }

    @Override
    public MissionDto completeMission(int id) {
        var mission = getMission(id);
        mission.setStatus(MissionStatus.COMPLETED);
        var savedMission = missionRepository.save(mission);

        var inventory = getInventory(mission.getProduct().getId(), mission.getWarehouse().getId());
        inventory.setCount(mission.getFinalCount());
        inventoryRepository.save(inventory);

        return missionMapper.toMissionDto(savedMission);
    }

    private User getUser(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

    private Mission getMission(int id) {
        return missionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.MISSION_NOT_FOUND, id)));
    }

    private Product getProduct(int id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, id)));
    }

    private Warehouse getWarehouse(int id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.WAREHOUSE_NOT_FOUND, id)));
    }

    private Inventory getInventory(int productId, int warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.INVENTORY_NOT_FOUND, productId, warehouseId)));
    }
}
