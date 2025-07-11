package com.example.inventory_service.service.impl.kafka;

import com.example.inventory_service.util.kafka.KafkaTopics;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WarehouseKafkaConsumer {
    @KafkaListener(topics = KafkaTopics.WAREHOUSES_TOPIC, groupId = "${kafka.consumer.warehouse.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
        System.out.println("Key: " + record.key());
    }
}
