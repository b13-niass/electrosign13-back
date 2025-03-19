package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.service.AuthenticationService;
import com.codev13.electrosign13back.web.controller.AuthenticationController;
import com.codev13.electrosign13back.web.dto.request.LoginRequestDto;
import com.codev13.electrosign13back.web.dto.request.RefreshTokenRequestDto;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
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
}
