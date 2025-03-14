package com.codev13.electrosign13back.client.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakRole {
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
}
