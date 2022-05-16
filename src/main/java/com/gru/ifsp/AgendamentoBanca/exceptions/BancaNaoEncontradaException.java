package com.gru.ifsp.AgendamentoBanca.exceptions;

public class BancaNaoEncontradaException extends RuntimeException{
    public BancaNaoEncontradaException(){
        super("Banca não encontrada");
    }
}
