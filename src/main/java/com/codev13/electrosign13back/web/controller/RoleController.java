package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.role.RoleDeleteRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleUpdateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface RoleController {
    ResponseEntity<RoleResponseDto> createRole(@RequestBody @Valid RoleRequestDto request);
    ResponseEntity<RoleResponseDto> updateRole(@RequestBody @Valid RoleUpdateRequestDto request);
    ResponseEntity<DeletedResponseDto> deleteRole(@RequestBody RoleDeleteRequestDto request);
}
