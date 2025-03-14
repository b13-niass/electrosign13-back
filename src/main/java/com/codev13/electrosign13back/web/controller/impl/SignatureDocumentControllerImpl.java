package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.SignatureDocument;
import com.codev13.electrosign13back.data.repository.SignatureDocumentRepository;
import com.codev13.electrosign13back.service.SignatureDocumentService;
import com.codev13.electrosign13back.web.controller.SignatureDocumentController;
import com.codev13.electrosign13back.web.dto.request.SignatureDocumentRequestDto;
import com.codev13.electrosign13back.web.dto.response.SignatureDocumentResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/signature-documents")
public class SignatureDocumentControllerImpl extends GenericController<SignatureDocument, SignatureDocumentResponseDto, SignatureDocumentRequestDto> implements SignatureDocumentController {
    private final SignatureDocumentService service;
    public SignatureDocumentControllerImpl(SignatureDocumentRepository repository, SignatureDocumentService service) {
        super(repository, SignatureDocument.class, SignatureDocumentResponseDto.class);
        this.service = service;
    }
}