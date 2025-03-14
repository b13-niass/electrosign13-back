package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Filiale;
import com.codev13.electrosign13back.data.repository.FilialeRepository;
import com.codev13.electrosign13back.service.FilialeService;
import com.codev13.electrosign13back.web.controller.FilialeController;
import com.codev13.electrosign13back.web.dto.request.FilialeRequestDto;
import com.codev13.electrosign13back.web.dto.response.FilialeResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/filiales")
public class FilialeControllerImpl extends GenericController<Filiale, FilialeResponseDto, FilialeRequestDto> implements FilialeController {
    private final FilialeService service;
    public FilialeControllerImpl(FilialeRepository repository, FilialeService service) {
        super(repository, Filiale.class, FilialeResponseDto.class);
        this.service = service;
    }
}