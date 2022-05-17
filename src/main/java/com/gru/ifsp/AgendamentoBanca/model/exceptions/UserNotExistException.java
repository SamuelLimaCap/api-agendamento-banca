package com.gru.ifsp.AgendamentoBanca.model.exceptions;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException(String message) {
        super(message);
    }
}
