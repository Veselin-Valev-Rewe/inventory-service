package com.example.inventory_service.message;

import com.example.inventory_service.message.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage<T> {
    private ActionType actionType;

    private T payload;
}
