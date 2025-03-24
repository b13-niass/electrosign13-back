package com.codev13.electrosign13back.exception.demande;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class DemandeGeneralException extends CustomRuntimeException {
    public DemandeGeneralException(String message, HttpStatus code) {
        super(message, code);
    }
}

