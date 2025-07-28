package com.example.inventory_service.config.security;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class KeycloakAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String clientId;

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> resourceAccess = source.getClaim("resource_access");

        if (resourceAccess == null || !resourceAccess.containsKey(clientId)) {
            return Collections.emptyList();
        }

        Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get(clientId);
        List<String> roles = (List<String>) clientRoles.get("roles");
        System.out.println("Parsed authorities: " + roles);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
