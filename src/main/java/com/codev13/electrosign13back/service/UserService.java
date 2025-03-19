package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto request);

}
