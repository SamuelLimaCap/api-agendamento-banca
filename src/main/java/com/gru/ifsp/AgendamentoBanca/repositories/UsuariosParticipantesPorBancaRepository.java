package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.model.UsuariosParticipantesBancaPK;
import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosParticipantesPorBancaRepository extends JpaRepository<UsuarioParticipantesPorBanca, UsuariosParticipantesBancaPK> {

    void deleteAllByBancaIs(AgendamentoBanca banca);

    @Query(value = "SELECT CASE WHEN IS_TEACHER = true THEN true ELSE false END FROM USUARIO_PARTICIPANTES_POR_BANCA WHERE USUARIO_ID = ?1 AND BANCA_ID = ?2", nativeQuery = true)
    boolean verifyUserIsTeacher(Long idUsuario, Long idBanca);


    @Query(value = "SELECT USUARIO_ID FROM USUARIO_PARTICIPANTES_POR_BANCA WHERE BANCA_ID = ?1 AND IS_ADMIN = true", nativeQuery = true)
    Long[] findAllByIsAdmin(Long idBanca);

    @Query(value = "SELECT USUARIO_ID FROM USUARIO_PARTICIPANTES_POR_BANCA WHERE BANCA_ID = ?1", nativeQuery = true)
    Long[] returAllMembersOnBanca(Long idBanca);

    List<UsuarioParticipantesPorBanca> getByIdAgendamentoBancaId(Long idBanca);

    @Query(value = "SELECT USUARIO_ID FROM USUARIO_PARTICIPANTES_POR_BANCA WHERE BANCA_ID = ?1 AND IS_ADMIN = true", nativeQuery = true)
    Long[] findAllByIsAdmin(Long idBanca);

    Optional<List<UsuarioParticipantesPorBanca>> findAllByIdAgendamentoBancaIdAndStatusAgendamentoEquals(Long banca_id, StatusAgendamento statusAgendamento);

    List<UsuarioParticipantesPorBanca> findAllByBancaIsAndIsTeacher(AgendamentoBanca banca, boolean isTeacher);

    Optional<UsuarioParticipantesPorBanca> findByBancaAndUsuario(AgendamentoBanca banca, Usuario usuario);

    }
