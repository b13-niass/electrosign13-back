package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.data.repository.DemandeRepository;
import com.codev13.electrosign13back.enums.TypeDocument;
import com.codev13.electrosign13back.service.DemandeService;
import com.codev13.electrosign13back.web.controller.DemandeController;
import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.request.DemandeRequestDto;
import com.codev13.electrosign13back.web.dto.request.SignerDemandeRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseListDto;
import com.codev13.electrosign13back.web.dto.response.DocumentBase64Dto;
import com.core.communs.core.GenericController;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/private/demandes")
public class DemandeControllerImpl extends GenericController<Demande, DemandeResponseDto, DemandeRequestDto> implements DemandeController {
    private final DemandeService service;
    public DemandeControllerImpl(DemandeRepository repository, DemandeService service) {
        super(repository, Demande.class, DemandeResponseDto.class);
        this.service = service;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<DemandeResponseDto> create(@RequestBody @Valid DemandeCreateRequestDto request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/envoyees")
    public ResponseEntity<List<DemandeResponseListDto>> getDemandesEnvoyees() {
        return ResponseEntity.ok(service.getDemandesEnvoyees());
    }

    @GetMapping("/recues")
    public ResponseEntity<List<DemandeResponseListDto>> getDemandesRecues() {
        return ResponseEntity.ok(service.getDemandesRecues());
    }

    @PostMapping(path = "/signer", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<DemandeResponseListDto> signerDemande(@ModelAttribute @Valid SignerDemandeRequestDto request) {
        return ResponseEntity.ok(service.signerDemande(request));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        List<DemandeResponseListDto> envoyees = service.getDemandesEnvoyees();
        List<DemandeResponseListDto> recues = service.getDemandesRecues();

        Map<String, Object> response = Map.of(
                "envoyees", envoyees,
                "recues", recues,
                "totalEnvoyees", envoyees.size(),
                "totalRecues", recues.size()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeResponseListDto> getDemandeById(@PathVariable Long id) {
        DemandeResponseListDto demande = service.getDemandeById(id);
        return ResponseEntity.ok(demande);
    }

    @GetMapping("/{id}/approuver")
    public ResponseEntity<DemandeResponseListDto> approuverDemande(@PathVariable Long id) {
        DemandeResponseListDto demande = service.approuverDemande(id);
        return ResponseEntity.ok(demande);
    }
    @GetMapping("/{id}/rejeter")
    public ResponseEntity<DemandeResponseListDto> rejeterDemande(@PathVariable Long id) {
        DemandeResponseListDto demande = service.rejeterDemande(id);
        return ResponseEntity.ok(demande);
    }

    @GetMapping("/{id}/document")
    public ResponseEntity<DocumentBase64Dto> getDocumentByDemandeId(
            @PathVariable Long id,
            @RequestParam(required = false) TypeDocument type) {

        Document document = service.getDocumentByDemandeId(id, type);
        if (document != null && document.getContenu() != null && !document.getContenu().isEmpty()) {
            String base64Encoded = document.getContenu();

            DocumentBase64Dto response = new DocumentBase64Dto(
                    document.getNom(),
                    document.getType().toString(),
                    base64Encoded
            );

            return ResponseEntity.ok(response);
        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getNom());
//
//        if (document.getContenu() != null && !document.getContenu().isEmpty()) {
//            byte[] data = Base64.getDecoder().decode(document.getContenu());
//            ByteArrayResource resource = new ByteArrayResource(data);
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(data.length)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//        }

//        if (document.getLinkCloud() != null && !document.getLinkCloud().isEmpty()) {
//            headers.add(HttpHeaders.LOCATION, document.getLinkCloud());
//            return ResponseEntity.status(302).headers(headers).build(); // Redirection
//        }

//        if (document.getLinkLocal() != null && !document.getLinkLocal().isEmpty()) {
//            try {
//                byte[] data = Files.readAllBytes(Paths.get(document.getLinkLocal()));
//                ByteArrayResource resource = new ByteArrayResource(data);
//
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .contentLength(data.length)
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .body(resource);
//            } catch (IOException e) {
//                throw new RuntimeException("Erreur lors de la lecture du fichier: " + e.getMessage());
//            }
//        }

        return ResponseEntity.notFound().build();
    }
}