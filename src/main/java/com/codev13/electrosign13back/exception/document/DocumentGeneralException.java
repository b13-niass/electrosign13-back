package com.codev13.electrosign13back.exception.document;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DocumentGeneralException extends CustomRuntimeException {
    public DocumentGeneralException(String message, HttpStatus code) {
        super(message, code);
    }
}

