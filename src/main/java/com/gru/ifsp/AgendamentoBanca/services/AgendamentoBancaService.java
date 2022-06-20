package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.form.AgendamentoUsuariosForm;
import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;

import java.util.List;


public interface AgendamentoBancaService {
    AgendamentoBanca add(AgendamentoBancaForm parametros) throws Exception;

    List<AgendamentoUsuariosForm> getAll();

    AgendamentoBanca getById(Long id);

    AgendamentoUsuariosForm getBancaAndBancaMembersByBancaId(Long id);

    AgendamentoBancaForm update(AgendamentoBancaForm parametros);

    String delete(Long id);

    AgendamentoBancaForm addParticipantes(AgendamentoBancaForm banca);

    boolean setSubscriptionStatus(Long bancaId, Long userId, StatusAgendamento newStatus);

    void updateUserForAdmin(Long idBanca, Long idUsuario, boolean permission);
}
