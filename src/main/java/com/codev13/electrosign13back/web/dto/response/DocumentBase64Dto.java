package com.codev13.electrosign13back.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentBase64Dto {
    private String nom;
    private String type;
    private String contenuBase64;
}
