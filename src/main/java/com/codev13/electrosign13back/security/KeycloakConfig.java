package com.codev13.electrosign13back.security;
import com.codev13.electrosign13back.client.keycloak.KeycloakAuthRequest;
import com.codev13.electrosign13back.client.keycloak.KeycloakClient;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}