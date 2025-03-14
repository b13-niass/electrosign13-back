package com.codev13.electrosign13back.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilialeResponseDto {
    private Long id;
    private String libelle;
    private String acronyme;
    private List<DepartementResponseDto> departements;
    private List<AnneeResponseDto> annees;
}
