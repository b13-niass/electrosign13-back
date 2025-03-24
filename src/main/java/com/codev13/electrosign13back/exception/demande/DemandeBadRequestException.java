package com.codev13.electrosign13back.exception.demande;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class DemandeBadRequestException extends CustomRuntimeException {
    public DemandeBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

