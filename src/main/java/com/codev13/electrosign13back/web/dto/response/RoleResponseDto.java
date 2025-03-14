package com.codev13.electrosign13back.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {
    private Long id;
    private String libelle;
    private List<UserResponseDto> users;
}
