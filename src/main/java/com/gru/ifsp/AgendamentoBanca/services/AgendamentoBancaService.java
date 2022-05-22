package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;

import java.util.List;


public interface AgendamentoBancaService {
    AgendamentoBanca add(AgendamentoBancaForm parametros) throws Exception;

    List<AgendamentoBanca> getAll();

    AgendamentoBanca getById(Long id);

    AgendamentoBancaForm update(AgendamentoBancaForm parametros, Long id);

    String delete(Long id);

    AgendamentoBancaForm addParticipantes(AgendamentoBancaForm banca);
}
