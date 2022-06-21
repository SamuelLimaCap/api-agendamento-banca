package com.gru.ifsp.AgendamentoBanca.util;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BancaUtils {

    public static void validateIfHasBancaCloseToThisTime(String dataAgendamento, Long bancaId, AgendamentoRepository repository) throws RuntimeException {
        var data = StringDateUtils.parseString(dataAgendamento);

        var bancasConflictedWithThisTime = repository
                .findByDataAgendamentoBetween(
                        data.minusMinutes(Constants.timeBetweenBancas),
                        data.plusMinutes(Constants.timeBetweenBancas)
                );

        if (bancaId != null) removeBancaItselfFromBancasConflicted(bancasConflictedWithThisTime, bancaId);

        if (bancasConflictedWithThisTime.size() > 0) {
            throw new RuntimeException("Banca não pode ser cadastrada neste horário! Há conflito de horário com outras bancas");
        }
    }

    private static void removeBancaItselfFromBancasConflicted(List<AgendamentoBanca> bancasConflictedWithThisTime, Long bancaId) {
        var actualBancaOnList = bancasConflictedWithThisTime.stream().filter(banca -> banca.getId().equals(bancaId)).collect(Collectors.toList());
        if (!actualBancaOnList.isEmpty()) {
            var banca = actualBancaOnList.get(0);
            bancasConflictedWithThisTime.remove(banca);
        }
    }

    public static void validateIfOneAdminIsNotAtLeastMeasurer(List<Long> adminsBanca, List<Usuario> measurers) {
        var possibleAdmins = measurers.stream().filter(measurer -> adminsBanca.contains(measurer.getId())).collect(Collectors.toList());
        if (possibleAdmins.isEmpty()) throw new RuntimeException("Os admins citados não fazem parte da lista de avaliadores cadastrados");
    }
}
