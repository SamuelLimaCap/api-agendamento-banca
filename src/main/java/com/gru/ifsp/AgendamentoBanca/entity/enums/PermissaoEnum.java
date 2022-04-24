package com.gru.ifsp.AgendamentoBanca.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissaoEnum implements GrantedAuthority {
    /*
    PERMISSÕES:
        Quando for adicionar, adicione também na classe "Code"
     */
    ADMIN(Code.ADMIN),
    USUARIO(Code.USUARIO);

    private final String authority;

    PermissaoEnum(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USUARIO = "ROLE_USUARIO";

    }
}
