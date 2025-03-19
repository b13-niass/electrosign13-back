package com.codev13.electrosign13back.exception.fonction;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class FonctionNotFoundException extends CustomRuntimeException {
    public FonctionNotFoundException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
