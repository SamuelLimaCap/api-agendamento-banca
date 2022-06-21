package com.gru.ifsp.AgendamentoBanca.util;

import com.gru.ifsp.AgendamentoBanca.dtos.UsuarioDto;
import com.gru.ifsp.AgendamentoBanca.form.BancaMemberDto;
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

    public static Map<String, List<BancaMemberDto>> splitMembersBasedOnBancaRoles(List<UsuarioParticipantesPorBanca> members) {
        var participants = new ArrayList<BancaMemberDto>();
        var measurers = new ArrayList<BancaMemberDto>();
        var bancaAdminList = new ArrayList<BancaMemberDto>();

        members.forEach(member -> {
            var user = member.getUsuario();
            var bancaMember = new BancaMemberDto(
                    user.getId(),
                    user.getEmail(),
                    user.getProntuario(),
                    user.getUsername(),
                    member.getStatusAgendamento(),
                    null
            );

            if (member.getIsStudent()) {
                bancaMember.setTipoMembro("participante");
                participants.add(bancaMember);
            } else if (member.getIsTeacher()) {
                bancaMember.setTipoMembro("avaliador");
                measurers.add(bancaMember);
            }

            if (member.getIsAdmin()) {
                var adminMember = bancaMember.clone();
                adminMember.setTipoMembro("adminDaBanca");
                bancaAdminList.add(adminMember);
            }
        });

        var memberSegmentedByRole = new HashMap<String, List<BancaMemberDto>>();
        memberSegmentedByRole.put("alunos", participants);
        memberSegmentedByRole.put("professores", measurers);
        memberSegmentedByRole.put("administradores", bancaAdminList);

        return memberSegmentedByRole;
    }
}
