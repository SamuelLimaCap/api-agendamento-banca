package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    Permissao getByCodeName(String codeName);
}
