package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.DemandeSignature;
import com.codev13.electrosign13back.data.repository.DemandeSignatureRepository;
import com.codev13.electrosign13back.service.DemandeSignatureService;
import com.codev13.electrosign13back.web.controller.DemandeSignatureController;
import com.codev13.electrosign13back.web.dto.request.DemandeSignatureRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeSignatureResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/demande-signatures")
public class DemandeSignatureControllerImpl extends GenericController<DemandeSignature, DemandeSignatureResponseDto, DemandeSignatureRequestDto> implements DemandeSignatureController {
    private final DemandeSignatureService service;
    public DemandeSignatureControllerImpl(DemandeSignatureRepository repository, DemandeSignatureService service) {
        super(repository, DemandeSignature.class, DemandeSignatureResponseDto.class);
        this.service = service;
    }
}