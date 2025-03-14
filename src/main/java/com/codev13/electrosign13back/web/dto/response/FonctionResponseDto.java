package com.codev13.electrosign13back.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FonctionResponseDto {
    private Long id;
    private String libelle;
    private String acronyme;
    private DepartementResponseDto departement;
    private List<UserResponseDto> users;
}
