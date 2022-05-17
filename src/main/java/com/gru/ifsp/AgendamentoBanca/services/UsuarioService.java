package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.model.Permissao;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UserNotExistException;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.response.UsuarioResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UserRepository usuarioRepository;
    private PermissaoRepository permissaoRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario createUser(UsuarioForm usuarioForm) throws EmailAlreadyExists {
        if (usuarioRepository.existsByEmail(usuarioForm.getEmail())) {
            throw new EmailAlreadyExists("Esse email já está cadastrado");
        }
        List<Permissao> permissaoList = List.of(permissaoRepository.getByCodeName(usuarioForm.getPermission().name()));
        String passwordEncoded = passwordEncoder.encode(usuarioForm.getPassword());

        Usuario usuario = new Usuario(null,
                usuarioForm.getEmail(),
                passwordEncoded,
                true,
                permissaoList,
                usuarioForm.getProntuario().toUpperCase(Locale.ROOT),
                usuarioForm.getUsername());

        usuarioRepository.save(usuario);
        return usuario;
    }

    public boolean disableUser(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            //TODO: fazer com que o usuário só se autentique quando estiver com isEnabled = true;
            Usuario usuario = usuarioRepository.findByEmail(email);
            usuarioRepository.delete(usuario);
        } else {
            throw new UserNotExistException("Este usuário não existe");
        }
        return true;
    }

    public List<UsuarioResponse> listUsers() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::new).collect(Collectors.toList());
    }


}
