package com.codev13.electrosign13back.exception.user;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class UserInternalServerException extends CustomRuntimeException {
    public UserInternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}