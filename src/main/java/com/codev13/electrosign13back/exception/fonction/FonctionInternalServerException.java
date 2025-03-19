package com.codev13.electrosign13back.exception.fonction;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class FonctionInternalServerException extends CustomRuntimeException {
    public FonctionInternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}