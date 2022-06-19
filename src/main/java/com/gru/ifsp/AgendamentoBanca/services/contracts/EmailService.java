package com.gru.ifsp.AgendamentoBanca.services.contracts;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;

public interface EmailService {

    boolean sendConfirmationCodeEmail(String email);

    boolean sendDifferencesBetweenNewAndOldBancaUpdate(AgendamentoBanca oldBanca, AgendamentoBanca newBanca);
}
