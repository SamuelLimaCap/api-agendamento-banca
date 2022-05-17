package com.gru.ifsp.AgendamentoBanca.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@Embeddable
public class UsuariosParticipantesBancaPK implements Serializable {

    @Column(name = "banca_id")
    Long agendamentoBancaId;

    @Column(name = "usuarios_id")
    Long usuarioId;

}
