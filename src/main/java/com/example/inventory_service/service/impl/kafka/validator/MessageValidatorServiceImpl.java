package com.example.inventory_service.service.impl.kafka.validator;

import com.example.inventory_service.service.MessageValidatorService;
import lombok.RequiredArgsConstructor;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageValidatorServiceImpl implements MessageValidatorService {

    private final Map<String, Schema> schemas;

    @Override
    public void validate(String schemaKey, String json) {
        var schema = schemas.get(schemaKey);

        if (schema == null) {
            throw new IllegalArgumentException("No schema registered for: " + schemaKey);
        }

        var jsonObject = new JSONObject(new JSONTokener(json));
        schema.validate(jsonObject);
    }
}
