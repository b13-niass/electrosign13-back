package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.response.DocumentDownloadDto;

import java.util.List;

public interface DocumentService {
    List<DocumentDownloadDto> getSignedDocumentsForDownload(Long demandeId);
    DocumentDownloadDto getDocumentForDownload(Long documentId);
}
