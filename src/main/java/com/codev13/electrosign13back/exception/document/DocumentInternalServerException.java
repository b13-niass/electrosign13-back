package com.codev13.electrosign13back.exception.document;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DocumentInternalServerException extends CustomRuntimeException {
    public DocumentInternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}