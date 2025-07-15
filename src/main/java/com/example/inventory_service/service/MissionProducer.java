package com.example.inventory_service.service;

import com.example.inventory_service.dto.mission.MissionMessageDto;
import com.example.inventory_service.message.Message;

public interface MissionProducer {
    void sentMessage(String key, Message<MissionMessageDto> message);
}
