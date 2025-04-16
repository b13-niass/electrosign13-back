package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.repository.RoleRepository;
import com.codev13.electrosign13back.service.RoleService;
import com.codev13.electrosign13back.web.controller.RoleController;

import com.codev13.electrosign13back.web.dto.request.role.RoleDeleteRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleUpdateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;
import com.core.communs.core.GenericController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/roles")
public class RoleControllerImpl extends GenericController<Role, RoleResponseDto, RoleRequestDto> implements RoleController {
    private final RoleService service;

    public RoleControllerImpl(RoleRepository repository, RoleService service) {
        super(repository, Role.class, RoleResponseDto.class);
        this.service = service;
    }

    @PostMapping("/private/keycloak/roles")
    @Override
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody @Valid RoleRequestDto request) {
        return ResponseEntity.ok(service.createRole(request));
    }

    @PutMapping("/private/keycloak/roles")
    @Override
    public ResponseEntity<RoleResponseDto> updateRole(@RequestBody @Valid RoleUpdateRequestDto request) {
        return ResponseEntity.ok(service.updateRole(request));
    }

    @DeleteMapping("/private/keycloak/roles")
    @Override
    public ResponseEntity<DeletedResponseDto> deleteRole(@RequestBody @Valid RoleDeleteRequestDto request) {
        return ResponseEntity.ok(service.deleteRole(request));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<RoleResponseDto>> getAllRole() {
        return ResponseEntity.ok(service.getAllRole());
    }
}