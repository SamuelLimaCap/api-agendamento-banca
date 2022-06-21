package com.gru.ifsp.AgendamentoBanca.form;

import com.gru.ifsp.AgendamentoBanca.dtos.UsuarioDto;
import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.util.StringDateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AgendamentoUsuariosForm {
    private Long id;
    private String titulo;
    private String descricao;
    private String tipoBanca;
    private String tema;
    private String dataAgendamento;
    private String statusAgendamento;
    private List<BancaMemberDto> listaParticipantes = new ArrayList<>();
    private List<BancaMemberDto> listaAvaliadores = new ArrayList<>();
    private List<BancaMemberDto> listaAdmins = new ArrayList<>();

    public AgendamentoUsuariosForm(AgendamentoBanca banca, List<BancaMemberDto> alunos, List<BancaMemberDto> professores, List<BancaMemberDto> listaAdmins){

        this.id = banca.getId();
        this.titulo = banca.getTitulo();
        this.descricao = banca.getDescricao();
        this.tipoBanca = String.valueOf(banca.getTipoBanca());
        this.tema = banca.getTema();
        this.dataAgendamento = banca.getDataAgendamento().format(StringDateUtils.formatter);
        this.statusAgendamento = String.valueOf(banca.getAgendamento());
        this.listaParticipantes.addAll(alunos);
        this.listaAvaliadores.addAll(professores);
        this.listaAdmins.addAll(listaAdmins);
    }


}
