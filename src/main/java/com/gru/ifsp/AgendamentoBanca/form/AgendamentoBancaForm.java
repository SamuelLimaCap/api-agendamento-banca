package com.gru.ifsp.AgendamentoBanca.form;

import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import com.gru.ifsp.AgendamentoBanca.model.enums.TipoBanca;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgendamentoBancaForm {
    private Long id;
    @NotBlank(message = "Título não informado!")
    private String titulo;
    @NotBlank(message = "Descrição não informada!")
    @Length(min = 2,message = "Quantidade mínima de caracteres não informada!")
    private String descricao;
    @NotNull(message = "Tipo de banca não informada!")
    private TipoBanca tipoBanca;
    @NotBlank(message = "Tema não informado!")
    @Length(min = 2,message = "Quantidade mínima de caracteres não informada!")
    private String tema;
    private String dataAgendamento;
    private Long[] listaIdParticipantes;
    private Long[] listaIdAvaliadores;
    private StatusAgendamento statusAgendamento;

    @NotNull(message = "É necessário informar os administradores da banca")
    @Size(min = 1, message = "Toda banca deve conter pelo menos 1 administrador!")
    private Long[] adminsBanca;


    public AgendamentoBancaForm(String titulo, String descricao, TipoBanca tipoBanca, String tema, String dataAgendamento, Long[] listaIdParticipantes, Long[] listaIdAvaliadores, StatusAgendamento statusAgendamento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoBanca = tipoBanca;
        this.tema = tema;
        this.dataAgendamento = dataAgendamento;
        this.listaIdParticipantes = listaIdParticipantes;
        this.listaIdAvaliadores = listaIdAvaliadores;
        this.statusAgendamento = statusAgendamento;
    }
}


