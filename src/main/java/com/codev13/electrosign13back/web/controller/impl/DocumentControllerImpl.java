package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.data.repository.DocumentRepository;
import com.codev13.electrosign13back.exception.document.DocumentNotFoundException;
import com.codev13.electrosign13back.service.DocumentService;
import com.codev13.electrosign13back.web.controller.DocumentController;
import com.codev13.electrosign13back.web.dto.request.DocumentRequestDto;
import com.codev13.electrosign13back.web.dto.response.DocumentDownloadDto;
import com.codev13.electrosign13back.web.dto.response.DocumentResponseDto;
import com.core.communs.core.GenericController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/private/documents")
@Slf4j
public class DocumentControllerImpl extends GenericController<Document, DocumentResponseDto, DocumentRequestDto> implements DocumentController {
    private final DocumentService service;
    private final DocumentRepository repository;
    private final ResourceLoader resourceLoader;

    @Value("${storage.local.path}")
    private String localPath;
    public DocumentControllerImpl(DocumentRepository repository, DocumentService service, ResourceLoader resourceLoader) {
        super(repository, Document.class, DocumentResponseDto.class);
        this.service = service;
        this.repository = repository;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Récupère les informations de téléchargement pour les documents signés d'une demande
     */
    @GetMapping("/demande/{demandeId}/download-info")
    public ResponseEntity<List<DocumentDownloadDto>> getDocumentsDownloadInfo(@PathVariable Long demandeId) {
        List<DocumentDownloadDto> downloadInfos = service.getSignedDocumentsForDownload(demandeId);
        return ResponseEntity.ok(downloadInfos);
    }


    /**
     * Récupère les informations de téléchargement pour un document spécifique
     */
    @GetMapping("/{documentId}/download-info")
    public ResponseEntity<DocumentDownloadDto> getDocumentDownloadInfo(@PathVariable Long documentId) {
        DocumentDownloadDto downloadInfo = service.getDocumentForDownload(documentId);
        return ResponseEntity.ok(downloadInfo);
    }

    /**
     * Télécharge un document stocké localement
     */
    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) throws IOException {
            // Récupérer le document
            Document document = repository.findById(documentId)
                    .orElseThrow(() -> new DocumentNotFoundException("Document non trouvé avec l'ID: " + documentId));

            // Construire le chemin du fichier
            String path = "classpath:uploads/" + document.getLinkLocal();
            Resource resource = resourceLoader.getResource(path);

            if (!resource.exists()) {
                log.error("Fichier non trouvé: {}", path);
                throw new DocumentNotFoundException("Fichier non trouvé: " + document.getLinkLocal());
            }
//        System.out.println(resource.getURL());

            // Configurer les en-têtes pour le téléchargement
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getNom() + "\"")
                    .body(resource);

    }
}