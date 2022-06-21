package com.gru.ifsp.AgendamentoBanca.annotations;

import com.gru.ifsp.AgendamentoBanca.model.enums.PermissaoEnum;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('" + PermissaoEnum.Code.ADMIN + "')" +
        "|| hasRole('" + PermissaoEnum.Code.COORDENADOR + "')" +
        "|| hasRole('" + PermissaoEnum.Code.PROFESSOR + "')" +
        "|| hasRole('" + PermissaoEnum.Code.ALUNO + "')"
)
public @interface IsAlunoOrAbove {
}
