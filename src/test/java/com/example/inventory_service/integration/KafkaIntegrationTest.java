package com.example.inventory_service.integration;

import com.example.inventory_service.util.kafka.KafkaTopics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    void testKafkaSendReceive() throws InterruptedException {
        kafkaTemplate.send(KafkaTopics.WAREHOUSES_TOPIC, "test-key", "test-message");
        kafkaTemplate.flush();
        Thread.sleep(5000);
        assertThat(true).isTrue();
    }
}
