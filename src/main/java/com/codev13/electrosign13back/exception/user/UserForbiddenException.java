package com.codev13.electrosign13back.exception.user;

import com.core.communs.web.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;
public class UserForbiddenException extends CustomRuntimeException {
    public UserForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
