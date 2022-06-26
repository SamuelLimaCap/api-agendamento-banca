package com.gru.ifsp.AgendamentoBanca.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailActivationCodeDto {

    private String email;
    private String activationCode;
}
