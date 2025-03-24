package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.UserLoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserController {
    ResponseEntity<UserResponseDto> createUser(UserRequestDto request);
    ResponseEntity<List<UserResponseDto>> getAllUsers(String roles, String status);
}
