package com.gru.ifsp.AgendamentoBanca.form;

import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BancaMemberDto implements Cloneable{

    private Long id;
    private String email;
    private String prontuario;
    private String username;
    private StatusAgendamento statusAgendamento;
    @Setter
    private String tipoMembro;

    @Override
    public BancaMemberDto clone() {
        try {
            BancaMemberDto clone = (BancaMemberDto) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
