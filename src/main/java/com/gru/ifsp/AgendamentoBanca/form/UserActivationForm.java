package com.gru.ifsp.AgendamentoBanca.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserActivationForm {

    public Long id;
    public String activationCode;
}
