package com.example.inventory_service.service.impl.kafka.consumer;

import com.example.inventory_service.data.entity.Warehouse;
import com.example.inventory_service.data.repository.WarehouseRepository;
import com.example.inventory_service.dto.warehouse.WarehouseDto;
import com.example.inventory_service.mapper.WarehouseMapper;
import com.example.inventory_service.message.Message;
import com.example.inventory_service.util.kafka.KafkaTopics;
import com.example.inventory_service.util.message.ErrorMessages;
import com.example.inventory_service.util.message.InfoMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseConsumer {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopics.WAREHOUSES_TOPIC, groupId = "${kafka.consumer.warehouse.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        log.info(InfoMessages.CONSUMING_MESSAGE, record.key(), record.value());

        try {
            var warehouseId = Integer.valueOf(record.key());
            var message = objectMapper.readValue(record.value(), Message.class);
            var warehouseDto = objectMapper.convertValue(message.getPayload(), WarehouseDto.class);
            var actionType = message.getActionType();

            switch (actionType) {
                case CREATE -> createWarehouse(warehouseDto);
                case UPDATE -> updateWarehouse(warehouseId, warehouseDto);
                case DELETE -> deleteWarehouse(warehouseId);
                default ->
                        throw new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, warehouseId));
            }
        } catch (Exception e) {
            log.error(ErrorMessages.CONSUMER_ERROR, e);
        }
    }

    private void createWarehouse(WarehouseDto warehouseDto) {
        var warehouse = warehouseMapper.toWarehouse(warehouseDto);
        warehouseRepository.save(warehouse);
    }

    private void deleteWarehouse(Integer warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, warehouseId));
        }
        warehouseRepository.deleteById(warehouseId);
    }

    private void updateWarehouse(Integer warehouseId, WarehouseDto warehouseDto) {
        var warehouse = getWarehouse(warehouseId);
        warehouseMapper.updateWarehouseFromDto(warehouseDto, warehouse);
        warehouseRepository.save(warehouse);
    }

    private Warehouse getWarehouse(int id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.WAREHOUSE_NOT_FOUND, id)));
    }
}
