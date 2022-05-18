package com.gru.ifsp.AgendamentoBanca.response;

import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class UsuarioResponse{

    private Long id;
    private String email;
    private boolean isEnabled;
    private String prontuario;
    private String username;


    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.isEnabled = usuario.isEnabled();
        this.prontuario = usuario.getProntuario();
        this.username = usuario.getUsername();
    }
}
