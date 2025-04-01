package com.codev13.electrosign13back.service;

import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.enums.TypeDocument;
import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseListDto;

import java.util.List;

public interface DemandeService {
    DemandeResponseDto create(DemandeCreateRequestDto request);
    List<DemandeResponseListDto> getDemandesEnvoyees();
    List<DemandeResponseListDto> getDemandesRecues();
    DemandeResponseListDto getDemandeById(Long demandeId);
    Document getDocumentByDemandeId(Long demandeId, TypeDocument type);
}
