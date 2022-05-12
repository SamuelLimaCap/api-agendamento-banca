package com.gru.ifsp.AgendamentoBanca.form;

import com.gru.ifsp.AgendamentoBanca.entity.enums.PermissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsuarioForm {
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String prontuario;
    @NotBlank
    private String username;

    private PermissaoEnum permission;



}
