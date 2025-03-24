package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.UserLoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto request);
    List<UserResponseDto> getAllUsers(
            String roles,
            String status);
}
