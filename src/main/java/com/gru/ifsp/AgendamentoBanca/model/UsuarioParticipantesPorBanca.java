package com.gru.ifsp.AgendamentoBanca.model;

import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
    @Nullable
    private Boolean isTeacher;

    @Nullable
    private Boolean isAdmin;

}
