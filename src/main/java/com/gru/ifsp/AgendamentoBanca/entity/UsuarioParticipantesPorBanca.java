package com.gru.ifsp.AgendamentoBanca.entity;

import com.gru.ifsp.AgendamentoBanca.entity.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UsuarioParticipantesPorBanca {

    @EmbeddedId
    UsuariosParticipantesBancaPK id = new UsuariosParticipantesBancaPK();

    @ManyToOne
    @MapsId("agendamentoBancaId")
    @JoinColumn(name = "banca_id")
    AgendamentoBanca banca;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    Usuario usuario;

    @Enumerated(EnumType.STRING)
    StatusAgendamento statusAgendamento = StatusAgendamento.AGUARDANDO;

    public UsuarioParticipantesPorBanca(AgendamentoBanca banca, Usuario usuario, StatusAgendamento statusAgendamento) {
        this.banca = banca;
        this.usuario = usuario;
        this.statusAgendamento = statusAgendamento;
    }
}
