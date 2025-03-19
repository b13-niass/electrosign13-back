package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.data.entity.Fonction;
import com.codev13.electrosign13back.data.entity.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String photo;
    private String telephone;
    private String publicKey;
    private String mySignature;
    private String fonction;
    private Set<Role> roles;
    private List<String> rolesLibelle;
}
