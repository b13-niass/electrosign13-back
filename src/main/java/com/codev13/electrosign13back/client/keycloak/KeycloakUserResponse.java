package com.codev13.electrosign13back.client.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakUserResponse {
    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private List<String> roles;
    private String firstName;
    private String lastName;
}
