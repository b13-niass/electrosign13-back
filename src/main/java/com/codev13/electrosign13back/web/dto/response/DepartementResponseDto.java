package com.codev13.electrosign13back.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartementResponseDto {
    private Long id;
    private String libelle;
    private String acronyme;
    private List<FilialeResponseDto> filiales;
    private List<FonctionResponseDto> fonctions;
}
