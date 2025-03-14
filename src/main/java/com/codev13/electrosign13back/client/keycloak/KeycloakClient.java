package com.codev13.electrosign13back.client.keycloak;

import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "keycloak", url = "${keycloak.auth-server-url}")
public interface KeycloakClient {
    @RequestMapping(value = "/realms/${keycloak.realm}/protocol/openid-connect/token", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponseDto getAccessToken(@RequestBody KeycloakAuthRequest authRequest);

    @RequestMapping(value = "/admin/realms/${keycloak.realm}/roles",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    KeycloakRole createRole(@RequestHeader("Authorization") String authHeader,
            @RequestBody KeycloakRole role);

    @PutMapping(value = "/admin/realms/${keycloak.realm}/roles/{roleName}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateRole(@RequestHeader("Authorization") String authHeader,
                    @PathVariable("roleName") String roleName,
                    @RequestBody KeycloakRole updatedRole);

    @DeleteMapping(value = "/admin/realms/${keycloak.realm}/roles/{roleName}")
    void deleteRole(@RequestHeader("Authorization") String authHeader,
                    @PathVariable("roleName") String roleName);
}
