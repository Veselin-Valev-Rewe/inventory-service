package com.example.inventory_service.service.impl.kafka.consumer;

import com.example.inventory_service.data.entity.Warehouse;
import com.example.inventory_service.data.repository.WarehouseRepository;
import com.example.inventory_service.dto.warehouse.WarehouseDto;
import com.example.inventory_service.mapper.WarehouseMapper;
import com.example.inventory_service.message.Message;
import com.example.inventory_service.util.errormessage.ErrorMessages;
import com.example.inventory_service.util.kafka.KafkaTopics;
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
        log.info("Consuming message with key={}, value = {}", record.key(), record.value());
        var warehouseId = Integer.valueOf(record.key());
        try {
            var message = objectMapper.readValue(record.value(), Message.class);
            var warehouseDto = objectMapper.convertValue(message.getPayload(), WarehouseDto.class);
            var actionType = message.getActionType();

            switch (actionType) {
                case CREATE -> warehouseRepository.save(warehouseMapper.toWarehouse(warehouseDto));
                case UPDATE -> {
                    var warehouse = getWarehouse(warehouseId);
                    warehouseMapper.updateWarehouseFromDto(warehouseDto, warehouse);
                    warehouseRepository.save(warehouse);
                }
                case DELETE -> {
                    if (!warehouseRepository.existsById(warehouseId)) {
                        throw new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, warehouseId));
                    }
                    warehouseRepository.deleteById(warehouseId);
                }
                default ->
                        throw new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, warehouseId));
            }
        } catch (Exception e) {
            log.error("Warehouse consumer error", e);
        }
    }

    private Warehouse getWarehouse(int id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.WAREHOUSE_NOT_FOUND, id)));
    }
}
