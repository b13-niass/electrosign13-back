package com.codev13.electrosign13back.exception.document;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DocumentForbiddenException extends CustomRuntimeException {
    public DocumentForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
