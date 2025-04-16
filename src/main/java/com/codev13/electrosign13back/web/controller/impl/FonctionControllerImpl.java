package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Fonction;
import com.codev13.electrosign13back.data.repository.FonctionRepository;
import com.codev13.electrosign13back.service.FonctionService;
import com.codev13.electrosign13back.web.controller.FonctionController;
import com.codev13.electrosign13back.web.dto.request.FonctionRequestDto;
import com.codev13.electrosign13back.web.dto.response.FonctionResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/private/fonctions")
public class FonctionControllerImpl extends GenericController<Fonction, FonctionResponseDto, FonctionRequestDto> implements FonctionController {
    private final FonctionService service;
    public FonctionControllerImpl(FonctionRepository repository, FonctionService service) {
        super(repository, Fonction.class, FonctionResponseDto.class);
        this.service = service;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<FonctionResponseDto>> getAllFonction() {
        return ResponseEntity.ok(service.getAllFonction());
    }
}