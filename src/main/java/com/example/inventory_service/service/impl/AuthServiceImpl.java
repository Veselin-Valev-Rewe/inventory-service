package com.example.inventory_service.service.impl;

import com.example.inventory_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RestClient restClient;

    @Value("${keycloak.token-uri}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.resource.jwt.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resource.jwt.client-secret}")
    private String clientSecret;

    @Override
    public String getUserAccessToken(String username, String password) {
        var form = new LinkedMultiValueMap<String, String>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("grant_type", "password");
        form.add("username", username);
        form.add("password", password);

        return getToken(form);
    }

    private String getToken(LinkedMultiValueMap<String, String> form) {
        var response = restClient
                .post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);


        if (response == null || response.get("access_token") == null) {
            throw new IllegalArgumentException("Access Token is null");
        }

        return response.get("access_token").toString();
    }
}
