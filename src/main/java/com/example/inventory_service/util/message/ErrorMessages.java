package com.example.inventory_service.util.message;

public class ErrorMessages {
    public static final String USER_NOT_FOUND = "User with id %d not found";
    public static final String WAREHOUSE_NOT_FOUND = "Warehouse with id %d not found";
    public static final String PRODUCT_NOT_FOUND = "Product with id %d not found";
    public static final String MISSION_NOT_FOUND = "Mission with id %d not found";
    public static final String INVENTORY_NOT_FOUND = "Inventory item with product id %d and warehouse id %d not found";
    public static final String UNSUPPORTED_OPERATION_TYPE = "Unsupported operation type: %S";
    public static final String SENDING_INTERRUPTED = "Kafka sending interrupted";
    public static final String SENDING_FAILED = "Kafka sending failed";
    public static final String CONSUMER_ERROR = "Consumer error";
    public static final String UNEXPECTED_VALUE = "Unexpected value: %S";
}
