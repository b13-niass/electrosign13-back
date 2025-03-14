package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.service.AuthenticationService;
import com.codev13.electrosign13back.web.controller.AuthenticationController;
import com.codev13.electrosign13back.web.dto.request.LoginRequestDto;
import com.codev13.electrosign13back.web.dto.request.RefreshTokenRequestDto;
import com.codev13.electrosign13back.web.dto.request.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/public/auth/login")
    @Override
    public ResponseEntity<LoginResponseDto> authUser(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(service.authUser(request));
    }

    @PostMapping("/public/auth/refresh-token")
    @Override
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @PostMapping("/private/keycloak/roles")
    @Override
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody @Valid RoleRequestDto request) {
        return ResponseEntity.ok(service.createRole(request));
    }

    @PutMapping("/private/keycloak/roles")
    @Override
    public ResponseEntity<RoleResponseDto> updateRole(@RequestBody @Valid RoleRequestDto request) {
        return ResponseEntity.ok(service.updateRole(request));
    }

    @DeleteMapping("/private/keycloak/roles")
    @Override
    public ResponseEntity<String> deleteRole(@RequestBody @Valid RoleRequestDto request) {
       return ResponseEntity.ok(service.deleteRole(request));
    }
}
