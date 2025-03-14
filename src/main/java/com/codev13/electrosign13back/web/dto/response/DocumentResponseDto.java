package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.enums.EtatDocument;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto {
    private Long id;
    private String nom;
    private String contenu;
    private EtatDocument etatDocument;
    private DemandeResponseDto demande;
}
