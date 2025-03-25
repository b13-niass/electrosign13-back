package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;

import java.util.List;

public interface DemandeService {
    DemandeResponseDto create(DemandeCreateRequestDto request);
    List<Object> getSentDemande();
    List<Object> getReceiveDemande();
}
