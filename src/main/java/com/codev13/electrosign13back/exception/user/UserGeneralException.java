package com.codev13.electrosign13back.exception.user;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class UserGeneralException extends CustomRuntimeException {
    public UserGeneralException(String message, HttpStatus code) {
        super(message, code);
    }
}

