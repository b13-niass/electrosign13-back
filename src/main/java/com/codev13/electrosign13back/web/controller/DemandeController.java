package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.request.SignerDemandeRequestDto;
import com.codev13.electrosign13back.web.dto.response.ArchiveResultDto;
import com.codev13.electrosign13back.web.dto.response.ArchiveStatsDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseListDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface DemandeController {
    ResponseEntity<DemandeResponseDto> create(@ModelAttribute @Valid DemandeCreateRequestDto request);
    ResponseEntity<Map<String, Object>> getDashboardData();
    ResponseEntity<List<DemandeResponseListDto>> getDemandesEnvoyees();
    ResponseEntity<List<DemandeResponseListDto>> getDemandesRecues();

    ResponseEntity<DemandeResponseListDto> signerDemande(SignerDemandeRequestDto request);
    ResponseEntity<DemandeResponseListDto> approuverDemande(@PathVariable Long id);
    ResponseEntity<DemandeResponseListDto> rejeterDemande(@PathVariable Long id);
    ResponseEntity<ArchiveResultDto> archiveDocuments();
    ResponseEntity<ArchiveStatsDto> getArchiveStats();
}
