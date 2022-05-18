package com.gru.ifsp.AgendamentoBanca.entity.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserDisabledException extends AuthenticationException {
    public UserDisabledException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserDisabledException(String msg) {
        super(msg);
    }
}
