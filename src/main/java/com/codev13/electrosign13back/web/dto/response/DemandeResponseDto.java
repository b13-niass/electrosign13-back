package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.enums.StatusDemande;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeResponseDto {
    private Long id;
    private String titre;
    private String description;
    private Date dateLimite;
    private int nombreApprobation;
    private int nombreSignature;
    private int currentApprobation;
    private int currentSignature;
    private StatusDemande status;
    private List<DemandeSignatureResponseDto> demandeSignatures;
    private List<DocumentResponseDto> documents;
}
