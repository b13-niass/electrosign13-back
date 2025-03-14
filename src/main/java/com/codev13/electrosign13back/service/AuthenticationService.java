package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.LoginRequestDto;
import com.codev13.electrosign13back.web.dto.request.RefreshTokenRequestDto;
import com.codev13.electrosign13back.web.dto.request.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;

public interface AuthenticationService {
    LoginResponseDto authUser(LoginRequestDto request);
    LoginResponseDto refreshToken(RefreshTokenRequestDto request);
    RoleResponseDto createRole(RoleRequestDto request);
    RoleResponseDto updateRole(RoleRequestDto request);
    String deleteRole(RoleRequestDto request);
}
