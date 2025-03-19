package com.codev13.electrosign13back.exception.user;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UserBadRequestException extends CustomRuntimeException {
    public UserBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

