package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.data.repository.UserRepository;
import com.codev13.electrosign13back.service.UserService;
import com.codev13.electrosign13back.web.controller.UserController;
import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/users")
public class UserControllerImpl extends GenericController<User, UserResponseDto, UserRequestDto> implements UserController {
    private final UserService service;
    public UserControllerImpl(UserRepository repository, UserService service) {
        super(repository, User.class, UserResponseDto.class);
        this.service = service;
    }
}
