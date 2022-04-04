package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;

import java.util.List;


public interface UsuarioService{

    Usuario Post(Usuario params);

    List<Usuario> Get();

    Usuario Get(Long id);

    Usuario atualizar(Usuario params, Long id);

    String excluir(Long id);
}
