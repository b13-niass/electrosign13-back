package com.codev13.electrosign13back.exception.document;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class DocumentNotFoundException extends CustomRuntimeException {
    public DocumentNotFoundException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
