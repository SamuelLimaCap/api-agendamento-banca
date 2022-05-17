package com.gru.ifsp.AgendamentoBanca.model.exceptions;

public class BancaNaoEncontradaException extends RuntimeException{
    public BancaNaoEncontradaException(){
        super("Banca não encontrada");
    }
}
