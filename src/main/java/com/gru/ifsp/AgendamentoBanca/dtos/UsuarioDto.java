package com.gru.ifsp.AgendamentoBanca.dtos;

import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UsuarioDto {
    private Long id;
    private String email;
    private String prontuario;
    private String username;

    public UsuarioDto(Usuario usuario){
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.prontuario = usuario.getProntuario();
        this.username = usuario.getUsername();
    }
}
