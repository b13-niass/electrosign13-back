package com.codev13.electrosign13back.exception.fonction;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class FonctionBadRequestException extends CustomRuntimeException {
    public FonctionBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

