package com.codev13.electrosign13back.exception.role;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends CustomRuntimeException {
    public RoleNotFoundException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
