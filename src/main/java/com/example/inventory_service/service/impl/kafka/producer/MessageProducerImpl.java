package com.example.inventory_service.service.impl.kafka.producer;

import com.example.inventory_service.data.entity.ProducedMessage;
import com.example.inventory_service.data.enums.MessageStatus;
import com.example.inventory_service.data.repository.ProducedMessageRepository;
import com.example.inventory_service.dto.mission.MissionMessageDto;
import com.example.inventory_service.message.Message;
import com.example.inventory_service.service.MissionProducer;
import com.example.inventory_service.util.kafka.KafkaTopics;
import com.example.inventory_service.util.message.ErrorMessages;
import com.example.inventory_service.util.message.InfoMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducerImpl implements MissionProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProducedMessageRepository producedMessageRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void sentMessage(String key, Message<MissionMessageDto> message) {
        log.info(InfoMessages.SENDING_MESSAGE, key, KafkaTopics.INVENTORY_MISSION_TOPIC);
        try {
            var payload = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaTopics.INVENTORY_MISSION_TOPIC, key, payload).get();
            saveProducedMessage(key, message, MessageStatus.SENT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(ErrorMessages.SENDING_INTERRUPTED, e);
            saveProducedMessage(key, message, MessageStatus.FAILED);
        } catch (Exception e) {
            log.error(ErrorMessages.SENDING_FAILED, e);
            saveProducedMessage(key, message, MessageStatus.FAILED);
        }
    }

    private void saveProducedMessage(String key, Message<MissionMessageDto> message, MessageStatus status) {
        var producedMessage = ProducedMessage.builder()
                .msgKey(key)
                .payload(message.toString())
                .status(status)
                .build();

        producedMessageRepository.save(producedMessage);
    }
}
