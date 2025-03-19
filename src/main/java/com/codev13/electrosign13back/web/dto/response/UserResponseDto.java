package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.data.entity.Fonction;
import com.codev13.electrosign13back.data.entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String photo;
    private String telephone;
    private String publicKey;
    private String mySignature;
    private Fonction fonction;
    private Set<Role> roles;
}
