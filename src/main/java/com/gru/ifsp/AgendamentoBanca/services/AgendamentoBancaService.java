package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;

import java.util.List;


public interface AgendamentoBancaService {
    AgendamentoBanca Post(AgendamentoBanca parametros);

    List<AgendamentoBanca> Get();

    AgendamentoBanca Get(Long id);

    AgendamentoBanca Update(AgendamentoBanca parametros, Long id);

    String Delete(Long id);
}
