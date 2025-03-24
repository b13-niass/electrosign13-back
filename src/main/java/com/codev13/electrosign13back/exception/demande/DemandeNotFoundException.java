package com.codev13.electrosign13back.exception.demande;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class DemandeNotFoundException extends CustomRuntimeException {
    public DemandeNotFoundException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
