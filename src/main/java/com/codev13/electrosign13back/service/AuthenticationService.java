package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.LoginRequestDto;
import com.codev13.electrosign13back.web.dto.request.RefreshTokenRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleDeleteRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleUpdateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.LoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;

public interface AuthenticationService {
    LoginResponseDto authUser(LoginRequestDto request);
    LoginResponseDto refreshToken(RefreshTokenRequestDto request);

}
