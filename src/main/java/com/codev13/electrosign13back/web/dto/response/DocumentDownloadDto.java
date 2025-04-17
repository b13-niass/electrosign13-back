package com.codev13.electrosign13back.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDownloadDto {
    private Long id;
    private String nom;
    private String url;
    private String contentType;
    private boolean isCloudDocument;
}