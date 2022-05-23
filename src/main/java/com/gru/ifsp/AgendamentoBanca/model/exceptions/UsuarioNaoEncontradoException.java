package com.gru.ifsp.AgendamentoBanca.model.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException{

    public UsuarioNaoEncontradoException(){
        super("Usuário não encontrado!");
    }
}
