package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.model.UsuariosParticipantesBancaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosParticipantesPorBancaRepository extends JpaRepository<UsuarioParticipantesPorBanca, UsuariosParticipantesBancaPK> {

}
