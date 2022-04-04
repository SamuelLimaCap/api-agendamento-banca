package com.gru.ifsp.AgendamentoBanca.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class AgendamentoBanca {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private TipoBanca tipoBanca;
    private String tema;
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataCadastro;
    @OneToMany
    private List<Usuario> participantes = new ArrayList<>();
    @OneToMany
    private List<Usuario> avaliadores = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private StatusAgendamento agendamento;

}


