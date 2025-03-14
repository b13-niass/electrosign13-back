package com.codev13.electrosign13back.exception.role;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class RoleGeneralException extends CustomRuntimeException {
    public RoleGeneralException(String message, HttpStatus code) {
        super(message, code);
    }
}

