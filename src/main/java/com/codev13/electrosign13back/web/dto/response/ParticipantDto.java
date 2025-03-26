package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.enums.DemandeSignatureActions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean hasSigned;
    private boolean isCurrentSigner;
    private int ordre;
    private String action;
}

