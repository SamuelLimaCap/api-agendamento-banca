package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.Permissao;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.entity.enums.PermissaoEnum;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UsuarioService {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario createUser(UsuarioForm usuarioForm) {
        List<Permissao> permissaoList = List.of(permissaoRepository.getByCodeName(usuarioForm.getPermission().name()));
        String passwordEncoded = passwordEncoder.encode(usuarioForm.getPassword());


        Usuario usuario = new Usuario(null,
                usuarioForm.getEmail(),
                passwordEncoded,
                true,
                permissaoList,
                usuarioForm.getUsername(),
                usuarioForm.getProntuario());

        usuarioRepository.save(usuario);
        return usuario;
    }


}
