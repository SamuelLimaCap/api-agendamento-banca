package com.gru.ifsp.AgendamentoBanca.entity.exceptions;

public class ProntuarioAlreadyExists extends RuntimeException{

    public ProntuarioAlreadyExists(String message) {
        super(message);
    }
}
