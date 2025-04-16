package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.client.keycloak.KeycloakAuthRequest;
import com.codev13.electrosign13back.client.keycloak.KeycloakClient;
import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.data.repository.RoleRepository;
import com.codev13.electrosign13back.data.repository.UserRepository;
import com.codev13.electrosign13back.exception.user.UserNotFoundException;
import com.codev13.electrosign13back.security.KeycloakProperties;
import com.codev13.electrosign13back.service.AuthenticationService;
import com.codev13.electrosign13back.service.TokenProvider;
import com.codev13.electrosign13back.web.dto.request.LoginRequestDto;
import com.codev13.electrosign13back.web.dto.request.RefreshTokenRequestDto;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserLoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import com.core.communs.service.MapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final KeycloakClient keycloakClient;
    private final KeycloakProperties keycloakProperties;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public LoginResponseDto authUser(LoginRequestDto request) {
        User user = userRepository.findByEmailAndActive(request.email(), true).orElseThrow(
                () -> new UserNotFoundException(request.email())
        );
        KeycloakAuthRequest keycloakAuthRequest = KeycloakAuthRequest.builder()
                .username(request.email())
                .password(request.password())
                .client_id(keycloakProperties.getClientId())
                .client_secret(keycloakProperties.getClientSecret())
                .grant_type(keycloakProperties.getGrant_type())
                .build();

        LoginResponseDto loginResponseDto = keycloakClient.getAccessToken(keycloakAuthRequest);
        var fonctionLibelle = user.getFonction().getLibelle();
        user.setFonction(null);
        UserLoginResponseDto userResponseDto = MapperService.mapToEntity(user, UserLoginResponseDto.class);
        userResponseDto.setFonction(fonctionLibelle);
        userResponseDto.setRolesLibelle(user.getRoles().stream().map(Role::getLibelle).collect(Collectors.toList()));
        loginResponseDto.setUser(userResponseDto);
        return loginResponseDto;
    }

    @Override
    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        KeycloakAuthRequest keycloakAuthRequest = KeycloakAuthRequest.builder()
                .refresh_token(request.refreshToken())
                .client_id(keycloakProperties.getClientId())
                .client_secret(keycloakProperties.getClientSecret())
                .grant_type("refresh_token")
                .build();
        return keycloakClient.getAccessToken(keycloakAuthRequest);
    }

}
