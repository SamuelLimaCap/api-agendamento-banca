package com.gru.ifsp.AgendamentoBanca.util;

import com.gru.ifsp.AgendamentoBanca.dtos.UsuarioDto;
import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioParticipantesPorBancaUtils {

    public static Map<String, List<UsuarioDto>> splitIntoBancaRoles(List<UsuarioParticipantesPorBanca> members, Long idBanca) {
        List<UsuarioDto> alunos = new ArrayList<>();
        List<UsuarioDto> professores = new ArrayList<>();
        var administradores = new ArrayList<UsuarioDto>();

        members.forEach(member -> {
            if (member.getIsTeacher()) {
                professores.add(new UsuarioDto(member.getUsuario()));
            } else if (member.getIsStudent()) {
                alunos.add(new UsuarioDto(member.getUsuario()));
            }

            if (member.getIsAdmin()) {
                administradores.add(new UsuarioDto(member.getUsuario()));
            }
        });


        Map<String, List<UsuarioDto>> membersSegmentedByRole = new HashMap<>();
        membersSegmentedByRole.put("alunos", alunos);
        membersSegmentedByRole.put("professores", professores);
        membersSegmentedByRole.put("administradores", administradores);

        return membersSegmentedByRole;
    }
}
