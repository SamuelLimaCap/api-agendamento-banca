package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.entity.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.entity.UsuariosParticipantesBancaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosParticipantesPorBancaRepository extends JpaRepository<UsuarioParticipantesPorBanca, UsuariosParticipantesBancaPK> {

}
