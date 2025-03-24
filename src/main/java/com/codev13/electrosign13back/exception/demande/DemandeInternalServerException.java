package com.codev13.electrosign13back.exception.demande;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DemandeInternalServerException extends CustomRuntimeException {
    public DemandeInternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}