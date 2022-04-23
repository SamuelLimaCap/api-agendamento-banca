package com.gru.ifsp.AgendamentoBanca.entity;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class AgendamentoBanca {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Título não informado!")
    private String titulo;
    @NotBlank(message = "Descrição não informada!")
    @Length(min = 2,message = "Quantidade mínima de caracteres não informada!")
    private String descricao;
    @NotBlank(message = "Tipo de banca não informada!")
    @Enumerated(EnumType.STRING)
    private TipoBanca tipoBanca;
    @NotBlank(message = "Tema não informado!")
    @Length(min = 2,message = "Quantidade mínima de caracteres não informada!")
    private String tema;
    @Future
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataCadastro;
    @OneToMany
    private List<Usuario> participantes = new ArrayList<>();
    @OneToMany
    private List<Usuario> avaliadores = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private StatusAgendamento agendamento;

    public AgendamentoBanca(String titulo, String descricao, TipoBanca tipoBanca,
                            String tema, LocalDateTime dataAgendamento,
                            LocalDateTime dataCadastro,
                            StatusAgendamento agendamento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoBanca = tipoBanca;
        this.tema = tema;
        this.dataAgendamento = dataAgendamento;
        this.dataCadastro = dataCadastro;
        this.agendamento = agendamento;
    }
}


