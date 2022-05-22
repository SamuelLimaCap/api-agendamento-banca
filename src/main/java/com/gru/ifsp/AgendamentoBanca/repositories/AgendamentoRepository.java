package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoBanca, Long> {

    @Query(value = "SELECT * FROM  AGENDAMENTO_BANCA  WHERE DATA_AGENDAMENTO = ?1 AND ", nativeQuery = true)
    Boolean verifyDataAgendamento(LocalDateTime dataAgendamento);
}
