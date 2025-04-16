package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserLoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDtoNew;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserController {
    ResponseEntity<UserResponseDto> createUser(UserRequestDto request);
    ResponseEntity<List<UserResponseDto>> getAllUsers(String roles, String status);
    ResponseEntity<List<UserResponseDtoNew>> getUsers();
    ResponseEntity<DeletedResponseDto> activerUser(Long idUser);
    ResponseEntity<DeletedResponseDto> desactiverUser(@PathVariable Long id);
}
