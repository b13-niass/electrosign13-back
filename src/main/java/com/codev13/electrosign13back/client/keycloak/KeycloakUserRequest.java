package com.codev13.electrosign13back.client.keycloak;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeycloakUserRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private List<Credential> credentials;
//    private List<String> roles;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Credential {
        private String type;
        private String value;
        private boolean temporary;
    }
}
