package com.codev13.electrosign13back.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveResultDto {
    private int totalDocuments;
    private List<String> successfullyArchived;
    private List<String> failedToArchive;
}
