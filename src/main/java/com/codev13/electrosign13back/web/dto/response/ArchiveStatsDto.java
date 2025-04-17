package com.codev13.electrosign13back.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveStatsDto {
    private int signedDocuments;
    private int archivedDocuments;
}
