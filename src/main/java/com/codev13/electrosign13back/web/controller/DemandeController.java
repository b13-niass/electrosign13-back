package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseListDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

public interface DemandeController {
    ResponseEntity<DemandeResponseDto> create(@ModelAttribute @Valid DemandeCreateRequestDto request);
    ResponseEntity<Map<String, Object>> getDashboardData();
    ResponseEntity<List<DemandeResponseListDto>> getDemandesEnvoyees();
    ResponseEntity<List<DemandeResponseListDto>> getDemandesRecues();
}
