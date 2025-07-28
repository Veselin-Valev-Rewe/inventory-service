package com.example.inventory_service.service;

public interface AuthService {
    String getUserAccessToken(String username, String password);
}
