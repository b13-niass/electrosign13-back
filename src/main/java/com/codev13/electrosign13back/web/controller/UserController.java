package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<UserResponseDto> createUser(UserRequestDto request);
}
