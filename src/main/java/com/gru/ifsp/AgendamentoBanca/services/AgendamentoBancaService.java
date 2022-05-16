package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;

import java.util.List;


public interface AgendamentoBancaService {
    AgendamentoBanca add(AgendamentoBancaForm parametros) throws Exception;

    List<AgendamentoBanca> getAll();

    AgendamentoBanca getById(Long id);

    AgendamentoBanca update(AgendamentoBanca parametros, Long id);

    String delete(Long id);

    AgendamentoBanca addParticipantes(AgendamentoBancaForm banca);
}
