package com.example.inventory_service.service.impl;

import com.example.inventory_service.data.entity.*;
import com.example.inventory_service.data.enums.MissionStatus;
import com.example.inventory_service.data.enums.OperationType;
import com.example.inventory_service.data.repository.*;
import com.example.inventory_service.dto.mission.CreateMissionDto;
import com.example.inventory_service.dto.mission.MissionDto;
import com.example.inventory_service.dto.mission.UpdateMissionDto;
import com.example.inventory_service.mapper.MissionMapper;
import com.example.inventory_service.message.Message;
import com.example.inventory_service.message.enums.ActionType;
import com.example.inventory_service.service.MissionProducer;
import com.example.inventory_service.service.MissionService;
import com.example.inventory_service.util.message.ErrorMessages;
import io.micrometer.core.instrument.MeterRegistry;
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
    private final MissionProducer missionProducer;
    private final MissionMapper missionMapper;
    private final MeterRegistry meterRegistry;

    private static Mission buildMission(CreateMissionDto missionDto, User user, Warehouse warehouse, Product product) {
        return Mission.builder()
                .operationType(missionDto.getOperationType())
                .status(MissionStatus.IN_PROGRESS)
                .user(user)
                .warehouse(warehouse)
                .product(product)
                .build();
    }

    @Override
    public MissionDto createMission(CreateMissionDto missionDto) {
        var user = getUser(missionDto.getUserId());
        var warehouse = getWarehouse(missionDto.getWarehouseId());
        var product = getProduct(missionDto.getProductId());
        var operationType = missionDto.getOperationType();
        var inventory = handleInventory(operationType, warehouse, product);

        var mission = buildMission(missionDto, user, warehouse, product);

        executeMission(operationType, mission, missionDto.getOperationCount(), inventory.getCount());

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
    public void completeMission(int id) {
        var mission = getMission(id);

        updateInventory(mission);
        updateMission(mission);
        sendMissionMessage(mission);
        meterRegistry.counter("completed_missions").increment();
    }

    private Inventory handleInventory(OperationType operationType, Warehouse warehouse, Product product) {

        if (operationType == OperationType.INITIAL_PLACEMENT) {
            if (!inventoryRepository.existsByProductIdAndWarehouseId(product.getId(), warehouse.getId())) {
                inventoryRepository.save(
                        Inventory.builder()
                                .warehouse(warehouse)
                                .product(product)
                                .count(0)
                                .build());
            }
        }

        return getInventory(product.getId(), warehouse.getId());
    }

    private void executeMission(OperationType operationType, Mission mission, int operationCount, int invCount) {
        switch (operationType) {
            case INITIAL_PLACEMENT -> setProductCounts(0, operationCount, mission);
            case UPDATE -> setProductCounts(invCount, invCount + operationCount, mission);
            case UNSTOW -> setProductCounts(invCount, 0, mission);
            default ->
                    throw new IllegalArgumentException(String.format(ErrorMessages.UNSUPPORTED_OPERATION_TYPE, operationType));
        }
    }

    private void sendMissionMessage(Mission mission) {
        var missionMessage = missionMapper.toMissionMessage(mission);
        missionProducer.sentMessage(String.valueOf(mission.getId()), new Message<>(ActionType.CREATE, missionMessage));
    }

    private void updateMission(Mission mission) {
        mission.setStatus(MissionStatus.COMPLETED);
        missionRepository.save(mission);
    }

    private void updateInventory(Mission mission) {
        var inventory = getInventory(mission.getProduct().getId(), mission.getWarehouse().getId());
        inventory.setCount(mission.getFinalCount());
        inventoryRepository.save(inventory);
    }

    private void setProductCounts(int originalCount, int finalCount, Mission mission) {
        mission.setOriginalCount(originalCount);
        mission.setFinalCount(finalCount);
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
