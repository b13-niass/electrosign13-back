package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Annee;
import com.codev13.electrosign13back.data.repository.AnneeRepository;
import com.codev13.electrosign13back.service.AnneeService;
import com.codev13.electrosign13back.web.controller.AnneeController;
import com.codev13.electrosign13back.web.dto.request.AnneeRequestDto;
import com.codev13.electrosign13back.web.dto.response.AnneeResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/private/annees"))
public class AnneeControllerImpl extends GenericController<Annee, AnneeResponseDto, AnneeRequestDto> implements AnneeController {
    private final AnneeService service;
    public AnneeControllerImpl(AnneeRepository repository, AnneeService service) {
        super(repository, Annee.class, AnneeResponseDto.class);
        this.service = service;
    }
}