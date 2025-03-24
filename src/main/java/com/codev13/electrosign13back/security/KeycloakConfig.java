package com.codev13.electrosign13back.security;
import com.codev13.electrosign13back.client.keycloak.KeycloakAuthRequest;
import com.codev13.electrosign13back.client.keycloak.KeycloakClient;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {
    private final KeycloakClient keycloakClient;
    private final KeycloakProperties keycloakProperties;

//    @Bean
//    public String keycloakAdminToken() {
//        KeycloakAuthRequest authRequest = KeycloakAuthRequest.builder()
//                .username("admin")
//                .password("admin")
//                .client_id(keycloakProperties.getClientId())
//                .client_secret(keycloakProperties.getClientSecret())
//                .grant_type(keycloakProperties.getGrant_type())
//                .build();
//        LoginResponseDto response = keycloakClient.getAccessToken(authRequest);
//        return "Bearer " + response.getAccessToken();
//    }
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
}