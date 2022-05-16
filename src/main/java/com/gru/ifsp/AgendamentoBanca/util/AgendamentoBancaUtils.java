package com.gru.ifsp.AgendamentoBanca.util;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class AgendamentoBancaUtils {

    public static AgendamentoBanca convertFormToAgendamentoBanca(AgendamentoBancaForm form,
                                                                 List<Usuario> paricipantes,
                                                                 List<Usuario> avaliadores) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataAgendamento = LocalDateTime.parse(form.getDataAgendamento(), formatter);
//        LocalDateTime dataCadastro = LocalDateTime.parse(LocalDateTime.now().toString(), formatter);
        String dataCadastro = LocalDateTime.now().toString();


            return new AgendamentoBanca(
                (Objects.isNull(form.getId())) ? null : form.getId(),
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
