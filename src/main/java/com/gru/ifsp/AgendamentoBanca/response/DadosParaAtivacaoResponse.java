package com.gru.ifsp.AgendamentoBanca.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DadosParaAtivacaoResponse {

    private Long id;
    private String email;
    private String activationCode;
}
