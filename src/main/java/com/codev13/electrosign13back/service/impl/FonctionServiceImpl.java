package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.data.repository.FonctionRepository;
import com.codev13.electrosign13back.service.FonctionService;
import com.codev13.electrosign13back.web.dto.response.FonctionResponseDto;
import com.core.communs.service.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FonctionServiceImpl implements FonctionService {
    private final FonctionRepository repository;
    public List<FonctionResponseDto> getAllFonction(){
        return MapperService.mapToListEntity(repository.findAll(), FonctionResponseDto.class);
    }
}
