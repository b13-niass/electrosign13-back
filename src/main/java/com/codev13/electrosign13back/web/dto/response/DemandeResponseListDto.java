package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.enums.PriorityDemande;
import com.codev13.electrosign13back.enums.StatusDemande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeResponseListDto {
    private Long id;
    private String titre;
    private String description;
    private Date dateCreated;
    private LocalDate dateLimite;
    private StatusDemande status;
    private PriorityDemande priority;
    private String documentUrl;
    private List<ParticipantDto> signataires;
    private List<ParticipantDto> approbateurs;
    private List<ParticipantDto> ampliateurs;
    private UserInfoDto sender;
    private boolean isCurrentUserSigner;
    private boolean isCurrentUserApprobateur;
}


