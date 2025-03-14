package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Departement;
import com.codev13.electrosign13back.data.repository.DepartementRepository;
import com.codev13.electrosign13back.service.DepartementService;
import com.codev13.electrosign13back.web.controller.DepartementController;
import com.codev13.electrosign13back.web.dto.request.DepartementRequestDto;
import com.codev13.electrosign13back.web.dto.response.DepartementResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/departements")
public class DepartementControllerImpl extends GenericController<Departement, DepartementResponseDto, DepartementRequestDto> implements DepartementController {
    private final DepartementService service;
    public DepartementControllerImpl(DepartementRepository repository, DepartementService service) {
        super(repository, Departement.class, DepartementResponseDto.class);
        this.service = service;
    }
}