package com.example.inventory_service.service.impl.kafka.consumer;

import com.example.inventory_service.data.entity.ProducedMessage;
import com.example.inventory_service.data.entity.Product;
import com.example.inventory_service.data.enums.MessageStatus;
import com.example.inventory_service.data.repository.ProducedMessageRepository;
import com.example.inventory_service.data.repository.ProductRepository;
import com.example.inventory_service.dto.product.ProductDto;
import com.example.inventory_service.mapper.ProductMapper;
import com.example.inventory_service.message.Message;
import com.example.inventory_service.util.errormessage.ErrorMessages;
import com.example.inventory_service.util.kafka.KafkaTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductConsumer {
    private final ProductRepository productRepository;
    private final ProducedMessageRepository producedMessageRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopics.PRODUCTS_TOPIC, groupId = "${kafka.consumer.product.group-id}")
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        var message = objectMapper.readValue(record.value(), Message.class);
        var productDto = (ProductDto) message.getPayload();
        var actionType = message.getActionType();
        var producedMessage = ProducedMessage.builder()
                .msgKey(record.key())
                .payload(record.toString())
                .build();
        try {
            switch (actionType) {
                case CREATE -> productRepository.save(productMapper.toProduct(productDto));
                case UPDATE -> {
                    var product = getProduct(productDto.getId());
                    productMapper.updateProductFromDto(productDto, product);
                    productRepository.save(product);
                }
                case DELETE -> {
                    if (productRepository.existsById(productDto.getId())) {
                        productRepository.deleteById(productDto.getId());
                    }

                    throw new EntityNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND);
                }
                default -> throw new EntityNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND);
            }
            producedMessage.setStatus(MessageStatus.SENT);
        } catch (Exception e) {
            producedMessage.setStatus(MessageStatus.FAILED);
        }

        producedMessageRepository.save(producedMessage);
    }

    private Product getProduct(int id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, id)));
    }
}

