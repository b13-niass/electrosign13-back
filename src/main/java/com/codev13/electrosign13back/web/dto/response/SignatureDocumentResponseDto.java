package com.codev13.electrosign13back.web.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignatureDocumentResponseDto {
    private Long id;
    private String documentHash;
    private DocumentResponseDto document;
    private UserResponseDto user;
}
