package com.gru.ifsp.AgendamentoBanca.form;

import com.gru.ifsp.AgendamentoBanca.entity.enums.StatusAgendamento;
import com.gru.ifsp.AgendamentoBanca.entity.enums.TipoBanca;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgendamentoBancaForm {

    private String titulo;
    private String descricao;
    private TipoBanca tipoBanca;

    private String tema;
    private String dataAgendamento;
    private String dataCadastro;
    private List<Long> listaIdParticipantes;
    private List<Long> listaIdAvaliadores;
    private StatusAgendamento statusAgendamento;
}
