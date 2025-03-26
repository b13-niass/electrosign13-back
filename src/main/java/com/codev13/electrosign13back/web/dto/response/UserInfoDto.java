package com.codev13.electrosign13back.web.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
    private String acronyme;
}


