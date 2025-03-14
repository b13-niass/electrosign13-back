package com.codev13.electrosign13back.exception.role;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class RoleUnauthorizedException extends CustomRuntimeException {
    public RoleUnauthorizedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

