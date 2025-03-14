package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.data.repository.DocumentRepository;
import com.codev13.electrosign13back.service.DocumentService;
import com.codev13.electrosign13back.web.controller.DocumentController;
import com.codev13.electrosign13back.web.dto.request.DocumentRequestDto;
import com.codev13.electrosign13back.web.dto.response.DocumentResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/documents")
public class DocumentControllerImpl extends GenericController<Document, DocumentResponseDto, DocumentRequestDto> implements DocumentController {
    private final DocumentService service;
    public DocumentControllerImpl(DocumentRepository repository, DocumentService service) {
        super(repository, Document.class, DocumentResponseDto.class);
        this.service = service;
    }
}