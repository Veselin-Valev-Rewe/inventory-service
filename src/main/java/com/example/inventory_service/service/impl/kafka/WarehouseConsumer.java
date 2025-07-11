package com.example.inventory_service.service.impl.kafka;

import com.example.inventory_service.data.entity.ProducedMessage;
import com.example.inventory_service.data.entity.Warehouse;
import com.example.inventory_service.data.enums.MessageStatus;
import com.example.inventory_service.data.repository.ProducedMessageRepository;
import com.example.inventory_service.data.repository.WarehouseRepository;
import com.example.inventory_service.dto.warehouse.WarehouseDto;
import com.example.inventory_service.mapper.WarehouseMapper;
import com.example.inventory_service.message.KafkaMessage;
import com.example.inventory_service.util.errormessage.ErrorMessages;
import com.example.inventory_service.util.kafka.KafkaTopics;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WarehouseConsumer {
    private final WarehouseRepository warehouseRepository;
    private final ProducedMessageRepository producedMessageRepository;
    private final WarehouseMapper warehouseMapper;

    @KafkaListener(topics = KafkaTopics.WAREHOUSES_TOPIC, groupId = "${kafka.consumer.warehouse.group-id}")
    public void listen(ConsumerRecord<String, KafkaMessage<WarehouseDto>> record) {
        var WarehouseDto = record.value().getPayload();
        var actionType = record.value().getActionType();
        var producedMessage = ProducedMessage.builder()
                .msgKey(record.key())
                .payload(record.toString())
                .build();
        try {
            switch (actionType) {
                case CREATE -> warehouseRepository.save(warehouseMapper.toWarehouse(WarehouseDto));
                case UPDATE -> {
                    var Warehouse = getWarehouse(WarehouseDto.getId());
                    warehouseMapper.updateWarehouseFromDto(WarehouseDto, Warehouse);
                    warehouseRepository.save(Warehouse);
                }
                case DELETE -> {
                    if (warehouseRepository.existsById(WarehouseDto.getId())) {
                        warehouseRepository.deleteById(WarehouseDto.getId());
                    }

                    throw new EntityNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND);
                }
                default -> throw new EntityNotFoundException(ErrorMessages.WAREHOUSE_NOT_FOUND);
            }
            producedMessage.setStatus(MessageStatus.SENT);
        } catch (Exception e) {
            producedMessage.setStatus(MessageStatus.FAILED);
        }

        producedMessageRepository.save(producedMessage);
    }

    private Warehouse getWarehouse(int id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.WAREHOUSE_NOT_FOUND, id)));
    }
}
