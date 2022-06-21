package com.gru.ifsp.AgendamentoBanca.services.contracts;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;

public interface EmailService {

    boolean sendConfirmationCodeEmail(String email);

    boolean sendEmailToConfirmChangesOnBanca(String email, Long bancaId, Long userId);

    boolean sendDifferencesBetweenNewAndOldBancaUpdate(AgendamentoBanca oldBanca, AgendamentoBanca newBanca, String email);

    boolean sendCustomMessageEmail(String title, String description, String content, String email);
}
