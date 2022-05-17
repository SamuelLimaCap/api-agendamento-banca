package com.gru.ifsp.AgendamentoBanca.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissaoEnum implements GrantedAuthority {
    /*
    PERMISSÕES:
        Quando for adicionar, adicione também na classe "Code"
     */
    ADMIN(Code.ADMIN),
    USUARIO(Code.USUARIO),
    ALUNO(Code.ALUNO),
    PROFESSOR(Code.PROFESSOR),
    COORDENADOR(Code.COORDENADOR);


    private final String authority;

    PermissaoEnum(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String codeName() {
        return this.name();
    }

    public static class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USUARIO = "ROLE_USUARIO";

        public static final String ALUNO = "ROLE_ALUNO";

        public static final String PROFESSOR = "ROLE_PROFESSOR";

        public static final String COORDENADOR = "ROLE_COORDENADOR";

    }
}
