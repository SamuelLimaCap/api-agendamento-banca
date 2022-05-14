package com.gru.ifsp.AgendamentoBanca.response;

import com.gru.ifsp.AgendamentoBanca.entity.Permissao;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioResponse{

    private String email;
    private boolean isEnabled;
    private String prontuario;
    private String username;


    public UsuarioResponse(Usuario usuario) {
        this.email = usuario.getEmail();
        this.isEnabled = usuario.isEnabled();
        this.prontuario = usuario.getProntuario();
        this.username = usuario.getUsername();
    }
}
