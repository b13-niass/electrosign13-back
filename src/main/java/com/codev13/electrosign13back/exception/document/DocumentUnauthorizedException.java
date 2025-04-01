package com.codev13.electrosign13back.exception.document;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DocumentUnauthorizedException extends CustomRuntimeException {
    public DocumentUnauthorizedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

