package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Parametre;
import com.codev13.electrosign13back.data.repository.ParametreRepository;
import com.codev13.electrosign13back.service.ParametreService;
import com.codev13.electrosign13back.web.controller.ParametreController;
import com.codev13.electrosign13back.web.dto.request.ParametreRequestDto;
import com.codev13.electrosign13back.web.dto.response.ParametreResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/parametres")
public class ParametreControllerImpl extends GenericController<Parametre, ParametreResponseDto, ParametreRequestDto> implements ParametreController {
    private final ParametreService service;
    public ParametreControllerImpl(ParametreRepository repository, ParametreService service) {
        super(repository, Parametre.class, ParametreResponseDto.class);
        this.service = service;
    }
}