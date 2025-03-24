package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;

public interface DemandeService {
    DemandeResponseDto create(DemandeCreateRequestDto request);
}
