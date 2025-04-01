package com.codev13.electrosign13back.exception.document;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class DocumentBadRequestException extends CustomRuntimeException {
    public DocumentBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

