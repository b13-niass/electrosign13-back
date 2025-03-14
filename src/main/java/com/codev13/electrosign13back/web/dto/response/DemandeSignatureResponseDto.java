package com.codev13.electrosign13back.web.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeSignatureResponseDto {
    private Long id;
    private String action;
    private int ordre;
    private int detenant;
    private DemandeResponseDto demande;
    private UserResponseDto sender;
    private UserResponseDto receiver;
}
