package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.util.AgendamentoBancaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AgendamentoBancaServiceImpl implements AgendamentoBancaService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AgendamentoBanca add(AgendamentoBancaForm form) throws Exception {

        List<Usuario> avaliadoresUsuarios = userRepository.findAllById(form.getListaIdParticipantes());
        List<Usuario> participantesUsuarios = userRepository.findAllById(form.getListaIdAvaliadores());

        if (participantesUsuarios.size() != form.getListaIdParticipantes().size()) throw new Exception("Algum usuário participante nao foi encontrado");
        if (avaliadoresUsuarios.size() != form.getListaIdAvaliadores().size()) throw new Exception("Algum usuário avaliador nao foi encontrado");

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.createAgendamentoBancaFromForms(form, participantesUsuarios, avaliadoresUsuarios);


        agendamentoRepository.save(agendamentoBanca);
        return agendamentoBanca;
    }

    @Override
    public List<AgendamentoBanca> getAll() {
        return agendamentoRepository.findAll();
    }

    @Override
    public AgendamentoBanca getById(Long id) {
        return agendamentoRepository.findById(id).get();
    }

    @Override
    public AgendamentoBanca update(AgendamentoBanca parametros, Long id) {

        AgendamentoBanca agendamento = agendamentoRepository.findById(id).get();
        agendamento.setTitulo(parametros.getTitulo());
        agendamento.setDescricao(parametros.getDescricao());
        agendamento.setTipoBanca(parametros.getTipoBanca());
        agendamento.setTema(parametros.getTema());
        agendamento.setDataAgendamento(parametros.getDataAgendamento());
        agendamento.setParticipantes(parametros.getParticipantes());
        agendamento.setAvaliadores(parametros.getAvaliadores());
        //FIXME
       //agendamento.setStatusAgendamento(parametros.getStatusAgendamento());

        return agendamentoRepository.save(agendamento);
    }

    @Override
    public String delete(Long id) {
        agendamentoRepository.deleteById(id);
        return "Agendamento(" + id + ")" + " foi excluíddo!";
    }

}
