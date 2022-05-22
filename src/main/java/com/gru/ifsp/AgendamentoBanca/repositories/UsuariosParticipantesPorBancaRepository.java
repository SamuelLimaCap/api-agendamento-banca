package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.model.UsuariosParticipantesBancaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuariosParticipantesPorBancaRepository extends JpaRepository<UsuarioParticipantesPorBanca, UsuariosParticipantesBancaPK> {

    void deleteAllByBancaIs(AgendamentoBanca banca);
    @Query(value = "SELECT USUARIO_ID FROM USUARIO_PARTICIPANTES_POR_BANCA WHERE BANCA_ID = ?1", nativeQuery = true)
    Long[] returAllMembersOnBanca(Long idBanca);

}

