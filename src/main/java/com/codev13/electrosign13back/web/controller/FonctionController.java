package com.codev13.electrosign13back.web.controller;

import com.codev13.electrosign13back.web.dto.response.FonctionResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FonctionController {
    ResponseEntity<List<FonctionResponseDto>> getAllFonction();
}
