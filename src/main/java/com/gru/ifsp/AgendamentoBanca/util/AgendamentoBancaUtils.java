package com.gru.ifsp.AgendamentoBanca.util;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendamentoBancaUtils {

    public static AgendamentoBanca createAgendamentoBancaFromForms(AgendamentoBancaForm form,
                                                                   List<Usuario> paricipantes,
                                                                   List<Usuario> avaliadores) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataAgendamento = LocalDateTime.parse(form.getDataAgendamento(), formatter);
        LocalDateTime dataCadastro = LocalDateTime.parse(form.getDataCadastro(), formatter);

        return new AgendamentoBanca(
                null,
                form.getTitulo(),
                form.getDescricao(),
                form.getTipoBanca(),
                form.getTema(),
                dataAgendamento,
                dataCadastro,
                paricipantes,
                avaliadores,
                form.getStatusAgendamento()
        );




    }
}
