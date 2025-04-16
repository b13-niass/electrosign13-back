package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.data.repository.UserRepository;
import com.codev13.electrosign13back.service.UserService;
import com.codev13.electrosign13back.web.controller.UserController;
import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.*;
import com.core.communs.core.GenericController;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/users")
public class UserControllerImpl extends GenericController<User, UserResponseDto, UserRequestDto> implements UserController {
    private final UserService service;

    public UserControllerImpl(UserRepository repository, UserService service) {
        super(repository, User.class, UserResponseDto.class);
        this.service = service;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<UserResponseDto> createUser(@ModelAttribute @Valid UserRequestDto request) {
       return ResponseEntity.ok(service.createUser(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(name = "roles", required = false) String roles,
            @RequestParam(name = "status", required = false) String status) {
        return ResponseEntity.ok(service.getAllUsers(roles, status));
    }

    @Override
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserResponseDtoNew>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @GetMapping("/activerUser/{id}")
    @Override
    public ResponseEntity<DeletedResponseDto> activerUser(@PathVariable Long id) {
       return ResponseEntity.ok(service.activerUser(id));
    }
    @GetMapping("/desactiverUser/{id}")
    @Override
    public ResponseEntity<DeletedResponseDto> desactiverUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.desactiverUser(id));
    }
}
