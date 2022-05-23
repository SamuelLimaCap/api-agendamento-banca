package com.gru.ifsp.AgendamentoBanca.model.exceptions;

public class ProntuarioAlreadyExists extends RuntimeException{

    public ProntuarioAlreadyExists(String message) {
        super(message);
    }
}
