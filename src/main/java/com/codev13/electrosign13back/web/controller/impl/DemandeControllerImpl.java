package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.data.repository.DemandeRepository;
import com.codev13.electrosign13back.service.DemandeService;
import com.codev13.electrosign13back.web.controller.DemandeController;
import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.request.DemandeRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import com.core.communs.core.GenericController;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}