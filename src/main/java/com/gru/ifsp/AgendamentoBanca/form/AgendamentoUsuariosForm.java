package com.gru.ifsp.AgendamentoBanca.form;

import com.gru.ifsp.AgendamentoBanca.dtos.UsuarioDto;
import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
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
    private List<UsuarioDto> listaParticipantes = new ArrayList<>();
    private List<UsuarioDto> listaAvaliadores = new ArrayList<>();
    private List<UsuarioDto> listaAdmins = new ArrayList<>();

    private List<UsuarioDto> adminsBanca = new ArrayList<>();

    public AgendamentoUsuariosForm(AgendamentoBanca banca, List<UsuarioDto> alunos, List<UsuarioDto> professores, List<UsuarioDto> listaAdmins){

        this.id = banca.getId();
        this.titulo = banca.getTitulo();
        this.descricao = banca.getDescricao();
        this.tipoBanca = String.valueOf(banca.getTipoBanca());
        this.tema = banca.getTema();
        this.dataAgendamento = banca.getDataAgendamento().toString();
        this.statusAgendamento = String.valueOf(banca.getAgendamento());
        this.listaParticipantes.addAll(alunos);
        this.listaAvaliadores.addAll(professores);
        this.listaAdmins.addAll(listaAdmins);
    }


}
