package com.gru.ifsp.AgendamentoBanca.entity.exceptions;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException(String message) {
        super(message);
    }
}
