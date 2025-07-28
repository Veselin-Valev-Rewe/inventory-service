package com.example.inventory_service.controller;

import com.example.inventory_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    public final AuthService authService;

    @GetMapping("/token")
    public ResponseEntity<String> getUserAccessToken(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authService.getUserAccessToken(username, password));
    }
}
