package com.codev13.electrosign13back.exception.fonction;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class FonctionUnauthorizedException extends CustomRuntimeException {
    public FonctionUnauthorizedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

