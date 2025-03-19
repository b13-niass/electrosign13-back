package com.codev13.electrosign13back.client.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakRoleAssign {
    private String id;
    private String name;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
}
