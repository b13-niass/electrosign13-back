package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.client.keycloak.KeycloakClient;
import com.codev13.electrosign13back.client.keycloak.KeycloakRole;
import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.repository.RoleRepository;
import com.codev13.electrosign13back.exception.role.RoleInternalServerException;
import com.codev13.electrosign13back.exception.role.RoleNotFoundException;
import com.codev13.electrosign13back.service.RoleService;
import com.codev13.electrosign13back.service.TokenProvider;
import com.codev13.electrosign13back.web.dto.request.role.RoleDeleteRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleRequestDto;
import com.codev13.electrosign13back.web.dto.request.role.RoleUpdateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DeletedResponseDto;
import com.codev13.electrosign13back.web.dto.response.RoleResponseDto;
import com.core.communs.service.MapperService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final KeycloakClient keycloakClient;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    @Override
    public RoleResponseDto createRole(RoleRequestDto request) {

        KeycloakRole keycloakRole = KeycloakRole
                .builder()
                .name(request.libelle())
                .description("Un nouveau role")
                .composite(false)
                .clientRole(false)
                .containerId("")
                .build();
        keycloakClient.createRole(tokenProvider.getToken(),keycloakRole);
        Role role = roleRepository.save(MapperService.mapToEntity(request, Role.class));
        return MapperService.mapToEntity(role, RoleResponseDto.class);
    }

    @Transactional
    public RoleResponseDto updateRole(RoleUpdateRequestDto request) {

        try {

            Role role = roleRepository.findRoleByLibelle(request.libelle())
                    .orElseThrow(() -> new RoleNotFoundException("Role with name " + request.libelle() + " not found"));
            ;

            KeycloakRole updatedRole = KeycloakRole.builder()
                    .name(request.libelle())
                    .description("")
                    .composite(false)
                    .clientRole(false)
                    .containerId("")
                    .build();

            keycloakClient.updateRole(tokenProvider.getToken(), request.libelle(), updatedRole);
            role.update(MapperService.mapToEntity(request, Role.class));
            var roleUpdated = roleRepository.save(role);
            return MapperService.mapToEntity(roleUpdated, RoleResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public DeletedResponseDto deleteRole(RoleDeleteRequestDto request) {
        try {
            Role role = roleRepository.findRoleByLibelle(request.libelle()).orElseThrow(
                    () -> new RoleNotFoundException("Role with name " + request.libelle() + " not found")
            );
            keycloakClient.deleteRole(tokenProvider.getToken(), request.libelle());
            roleRepository.deleteByLibelle(request.libelle());
            return DeletedResponseDto.builder().message("deleted").build();
        }catch (Exception e){
            throw new RoleInternalServerException(e.getMessage());
        }
    }

    public List<RoleResponseDto> getAllRole(){
        return MapperService.mapToListEntity(roleRepository.findAll(), RoleResponseDto.class);
    }
}
