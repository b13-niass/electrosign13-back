package com.codev13.electrosign13back.exception.role;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class RoleForbiddenException extends CustomRuntimeException {
    public RoleForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
