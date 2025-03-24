package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface DemandeController {
    ResponseEntity<DemandeResponseDto> create(@ModelAttribute @Valid DemandeCreateRequestDto request);
}
