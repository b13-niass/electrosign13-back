package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.data.repository.DemandeRepository;
import com.codev13.electrosign13back.enums.StatusDemande;
import com.codev13.electrosign13back.enums.TypeDocument;
import com.codev13.electrosign13back.exception.document.DocumentNotFoundException;
import com.codev13.electrosign13back.service.DocumentService;
import com.codev13.electrosign13back.web.dto.response.DocumentDownloadDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {
    private final DemandeRepository demandeRepository;
    @Value("${app.base-url}")
    private String baseUrl;
    @Value("${storage.local.path}")
    private String localPath;

    /**
     * Récupère les informations de téléchargement pour les documents signés d'une demande
     * @param demandeId ID de la demande
     * @return Liste des DTOs contenant les informations de téléchargement
     */
    public List<DocumentDownloadDto> getSignedDocumentsForDownload(Long demandeId) {
        log.info("Récupération des documents signés pour la demande ID: {}", demandeId);

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DocumentNotFoundException("Demande non trouvée avec l'ID: " + demandeId));

        // Vérifier si la demande est signée
        if (demande.getStatus() != StatusDemande.SIGNEE) {
            log.warn("La demande avec l'ID {} n'est pas encore signée", demandeId);
            throw new IllegalStateException("La demande n'est pas encore signée");
        }

        // Filtrer les documents de type SIGNATURE
        List<Document> signedDocuments = demande.getDocuments().stream()
                .filter(doc -> doc.getType() == TypeDocument.SIGNATURE)
                .collect(Collectors.toList());

        if (signedDocuments.isEmpty()) {
            log.warn("Aucun document signé trouvé pour la demande ID: {}", demandeId);
            throw new DocumentNotFoundException("Aucun document signé trouvé pour cette demande");
        }

        // Convertir les documents en DTOs
        return signedDocuments.stream()
                .map(this::convertToDownloadDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les informations de téléchargement pour un document spécifique
     * @param documentId ID du document
     * @return DTO contenant les informations de téléchargement
     */
    public DocumentDownloadDto getDocumentForDownload(Long documentId) {
        log.info("Récupération du document pour téléchargement, ID: {}", documentId);

        // Trouver la demande qui contient ce document
        Optional<Demande> demandeOpt = demandeRepository.findAll().stream()
                .filter(demande -> demande.getDocuments().stream()
                        .anyMatch(doc -> doc.getId().equals(documentId)))
                .findFirst();

        if (demandeOpt.isEmpty()) {
            log.warn("Aucune demande trouvée contenant le document ID: {}", documentId);
            throw new DocumentNotFoundException("Document non trouvé avec l'ID: " + documentId);
        }

        Demande demande = demandeOpt.get();

        // Trouver le document dans la demande
        Optional<Document> documentOpt = demande.getDocuments().stream()
                .filter(doc -> doc.getId().equals(documentId))
                .findFirst();

        if (documentOpt.isEmpty()) {
            log.warn("Document non trouvé dans la demande, ID: {}", documentId);
            throw new DocumentNotFoundException("Document non trouvé avec l'ID: " + documentId);
        }

        return convertToDownloadDto(documentOpt.get());
    }

    /**
     * Convertit un document en DTO pour le téléchargement
     */
    private DocumentDownloadDto convertToDownloadDto(Document document) {
        String url;
        boolean isCloudDocument = false;

        // Vérifier si le document est stocké dans le cloud
//        if (StringUtils.hasText(document.getLinkCloud())) {
//            url = document.getLinkCloud();
//            isCloudDocument = true;
//        } else
            if (StringUtils.hasText(document.getLinkLocal())) {
            // Construire l'URL pour accéder au fichier local via l'API
            url = baseUrl + "/api/documents/download/" + document.getId();
        } else {
            log.error("Document sans lien de téléchargement, ID: {}", document.getId());
            throw new IllegalStateException("Le document n'a pas de lien de téléchargement");
        }

        // Déterminer le type de contenu
        String contentType = determineContentType(document.getNom());

        return DocumentDownloadDto.builder()
                .id(document.getId())
                .nom(document.getNom())
                .url(url)
                .contentType(contentType)
                .isCloudDocument(isCloudDocument)
                .build();
    }

    /**
     * Détermine le type de contenu en fonction de l'extension du fichier
     */
    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";
        }
    }
}
