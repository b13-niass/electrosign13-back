package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.role.RoleDeleteRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleUpdateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;

public interface RoleService {
    RoleResponseDto createRole(RoleRequestDto request);
    RoleResponseDto updateRole(RoleUpdateRequestDto request);
    DeletedResponseDto deleteRole(RoleDeleteRequestDto request);
}
