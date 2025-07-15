package com.example.inventory_service.service.impl.kafka.consumer;

import com.example.inventory_service.data.entity.Product;
import com.example.inventory_service.data.repository.ProductRepository;
import com.example.inventory_service.dto.product.ProductDto;
import com.example.inventory_service.mapper.ProductMapper;
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
public class ProductConsumer {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopics.PRODUCTS_TOPIC, groupId = "${kafka.consumer.product.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        log.info(InfoMessages.CONSUMING_MESSAGE, record.key(), record.value());

        try {
            var productId = Integer.valueOf(record.key());
            var message = objectMapper.readValue(record.value(), Message.class);
            var productDto = objectMapper.convertValue(message.getPayload(), ProductDto.class);
            var actionType = message.getActionType();

            switch (actionType) {
                case CREATE -> createProduct(productDto);
                case UPDATE -> updateProduct(productId, productDto);
                case DELETE -> deleteProduct(productId);
                default -> throw new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, productId));
            }
        } catch (Exception e) {
            log.error(ErrorMessages.CONSUMER_ERROR, e);
        }
    }

    private void deleteProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, productId));
        }
        productRepository.deleteById(productId);
    }

    private void updateProduct(Integer productId, ProductDto productDto) {
        var product = getProduct(productId);
        productMapper.updateProductFromDto(productDto, product);
        productRepository.save(product);
    }

    private void createProduct(ProductDto productDto) {
        var product = productMapper.toProduct(productDto);
        productRepository.save(product);
    }

    private Product getProduct(int id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.PRODUCT_NOT_FOUND, id)));
    }
}

