package com.codev13.electrosign13back.exception.demande;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DemandeForbiddenException extends CustomRuntimeException {
    public DemandeForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
