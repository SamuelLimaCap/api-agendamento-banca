package com.gru.ifsp.AgendamentoBanca.response;

import com.gru.ifsp.AgendamentoBanca.model.Permissao;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.enums.PermissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Getter
public class UsuarioResponse{

    private Long id;
    private String email;
    private boolean isEnabled;
    private String prontuario;
    private String username;
    @Setter
    private String permission = PermissaoEnum.ALUNO.name();


    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.isEnabled = usuario.isEnabled();
        this.prontuario = usuario.getProntuario();
        this.username = usuario.getUsername();

        setMaxPermission(usuario.getPermissaoList());

    }

    private void setMaxPermission(List<Permissao> permissionList) {
        permissionList.sort(Comparator.comparing(Permissao::getId));
        this.permission = permissionList.get(0).getCodeName();
    }
}
