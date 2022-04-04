package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Usuario Post(Usuario params) {
        usuarioRepository.save(params);
        return params;
    }

    @Override
    public List<Usuario> Get() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario Get(Long id) {
        return usuarioRepository.findById(id).get();
    }

    @Override
    public Usuario atualizar(Usuario params, Long id) {
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setEmail(params.getEmail());

        return usuarioRepository.save(usuario);
    }

    @Override
    public String excluir(Long id) {
       usuarioRepository.deleteById(id);
       return "Usuario(" + id + ")" + "foi excluído!";
    }
}
