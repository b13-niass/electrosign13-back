package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.repository.RoleRepository;
import com.codev13.electrosign13back.service.RoleService;
import com.codev13.electrosign13back.web.controller.RoleController;

import com.codev13.electrosign13back.web.dto.request.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/roles")
public class RoleControllerImpl extends GenericController<Role, RoleResponseDto, RoleRequestDto> implements RoleController {
    private final RoleService service;
    public RoleControllerImpl(RoleRepository repository, RoleService service) {
        super(repository, Role.class, RoleResponseDto.class);
        this.service = service;
    }
}