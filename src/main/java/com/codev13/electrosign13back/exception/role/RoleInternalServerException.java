package com.codev13.electrosign13back.exception.role;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class RoleInternalServerException extends CustomRuntimeException {
    public RoleInternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}