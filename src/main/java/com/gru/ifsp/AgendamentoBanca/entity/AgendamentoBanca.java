package com.gru.ifsp.AgendamentoBanca.entity;


import com.gru.ifsp.AgendamentoBanca.entity.enums.StatusAgendamento;
import com.gru.ifsp.AgendamentoBanca.entity.enums.TipoBanca;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    @NotNull(message = "Tipo de banca não informada!")
    @Enumerated(EnumType.STRING)
    private TipoBanca tipoBanca;
    @NotBlank(message = "Tema não informado!")
    @Length(min = 2,message = "Quantidade mínima de caracteres não informada!")
    private String tema;
    private LocalDateTime dataAgendamento;
    private String dataCadastro;

    @OneToMany(mappedBy = "banca", cascade = CascadeType.ALL)
    private List<Usuario> participantes = new ArrayList<>();
    @OneToMany(mappedBy = "banca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> avaliadores = new ArrayList<>();
    /*
    @OneToMany(mappedBy = "banca")
    List<AgendamentoUsuarios> banca;
    */
    @Enumerated(EnumType.STRING)
    private StatusAgendamento agendamento;

}


