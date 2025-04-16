package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserLoginResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDtoNew;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto request);
    List<UserResponseDto> getAllUsers(
            String roles,
            String status);
    List<UserResponseDtoNew> getUsers();
    DeletedResponseDto activerUser(Long idUser);
    DeletedResponseDto desactiverUser(@PathVariable Long id);
}
