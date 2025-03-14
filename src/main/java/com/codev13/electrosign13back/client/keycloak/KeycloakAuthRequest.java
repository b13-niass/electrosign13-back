package com.codev13.electrosign13back.client.keycloak;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeycloakAuthRequest {
    private String username;
    private String password;
    private String client_id;
    private String grant_type;
    public String client_secret;
    private String refresh_token;
}
