package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.response.DocumentDownloadDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

public interface DocumentController {
    ResponseEntity<List<DocumentDownloadDto>> getDocumentsDownloadInfo(@PathVariable Long demandeId);
    ResponseEntity<DocumentDownloadDto> getDocumentDownloadInfo(@PathVariable Long documentId);
    ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) throws IOException;
}
