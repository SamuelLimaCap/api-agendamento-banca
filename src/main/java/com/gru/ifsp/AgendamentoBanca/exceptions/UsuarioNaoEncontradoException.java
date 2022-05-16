package com.gru.ifsp.AgendamentoBanca.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException{

    public UsuarioNaoEncontradoException(){
        super("Usuário não encontrado!");
    }
}
