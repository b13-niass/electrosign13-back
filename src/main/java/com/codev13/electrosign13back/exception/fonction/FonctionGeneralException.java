package com.codev13.electrosign13back.exception.fonction;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class FonctionGeneralException extends CustomRuntimeException {
    public FonctionGeneralException(String message, HttpStatus code) {
        super(message, code);
    }
}

