package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoBanca, Long> {

    List<AgendamentoBanca> findByDataAgendamentoBetween(LocalDateTime dataAgendamento, LocalDateTime intervaloAgendamento);


}
