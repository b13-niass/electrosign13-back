package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.client.keycloak.KeycloakAuthRequest;
import com.codev13.electrosign13back.client.keycloak.KeycloakClient;
import com.codev13.electrosign13back.client.keycloak.KeycloakRole;
import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.repository.RoleRepository;
import com.codev13.electrosign13back.exception.role.RoleInternalServerException;
import com.codev13.electrosign13back.exception.role.RoleNotFoundException;
import com.codev13.electrosign13back.security.KeycloakProperties;
import com.codev13.electrosign13back.service.AuthenticationService;
import com.codev13.electrosign13back.service.TokenProvider;
import com.codev13.electrosign13back.web.dto.request.LoginRequestDto;
import com.codev13.electrosign13back.web.dto.request.RefreshTokenRequestDto;
import com.codev13.electrosign13back.web.dto.request.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;
import com.core.communs.service.MapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final KeycloakClient keycloakClient;
    private final KeycloakProperties keycloakProperties;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public LoginResponseDto authUser(LoginRequestDto request) {
        logger.info(request.username() +" "+ request.password());
        KeycloakAuthRequest keycloakAuthRequest = KeycloakAuthRequest.builder()
                .username(request.username())
                .password(request.password())
                .client_id(keycloakProperties.getClientId())
                .client_secret(keycloakProperties.getClientSecret())
                .grant_type(keycloakProperties.getGrant_type())
                .build();

        return keycloakClient.getAccessToken(keycloakAuthRequest);
    }

    @Override
    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        logger.info("Refreshing token for: " + request.refreshToken());

        KeycloakAuthRequest keycloakAuthRequest = KeycloakAuthRequest.builder()
                .refresh_token(request.refreshToken())
                .client_id(keycloakProperties.getClientId())
                .client_secret(keycloakProperties.getClientSecret())
                .grant_type("refresh_token")
                .build();

        return keycloakClient.getAccessToken(keycloakAuthRequest);
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto request) {

        KeycloakRole keycloakRole = KeycloakRole
                .builder()
                .name(request.libelle())
                .description("Un nouveau role")
                .composite(false)
                .clientRole(false)
                .containerId("")
                .build();
        System.out.println(tokenProvider.getToken());
        keycloakClient.createRole(tokenProvider.getToken(),keycloakRole);
        Role role = roleRepository.save(MapperService.mapToEntity(request, Role.class));
        return MapperService.mapToEntity(role, RoleResponseDto.class);
    }

    public RoleResponseDto updateRole(RoleRequestDto request) {

        try {

            Role role = roleRepository.findRoleByLibelle(request.libelle())
                    .orElseThrow(() -> new RoleNotFoundException("Role with name " + request.libelle() + " not found"));
            ;

            KeycloakRole updatedRole = KeycloakRole.builder()
                    .name(request.libelle())
                    .description("")
                    .composite(false)
                    .clientRole(false)
                    .containerId("")
                    .build();

            keycloakClient.updateRole(tokenProvider.getToken(), request.libelle(), updatedRole);
            role.update(MapperService.mapToEntity(request, Role.class));
            var roleUpdated = roleRepository.save(role);
            return MapperService.mapToEntity(roleUpdated, RoleResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteRole(RoleRequestDto request) {
        try {
            keycloakClient.deleteRole(tokenProvider.getToken(), request.libelle());
            roleRepository.deleteByLibelle(request.libelle());
            return "deleted";
        }catch (Exception e){
            throw new RoleInternalServerException(e.getMessage());
        }
    }

}
