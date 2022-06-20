package com.gru.ifsp.AgendamentoBanca.model.exceptions;

public class UsuarioNaoEncontradoNaBanca extends RuntimeException{

    public UsuarioNaoEncontradoNaBanca(){
        super("Usuário não encontrado para esta banca");
    }
}
