package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoBancaServiceImpl implements AgendamentoBancaService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoBancaServiceImpl(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Override
    public AgendamentoBanca Post(AgendamentoBanca parametros) {
        agendamentoRepository.save(parametros);
        return parametros;
    }

    @Override
    public List<AgendamentoBanca> Get() {
        return agendamentoRepository.findAll();
    }

    @Override
    public AgendamentoBanca Get(Long id) {
        return agendamentoRepository.findById(id).get();
    }

    @Override
    public AgendamentoBanca Update(AgendamentoBanca parametros, Long id) {

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
    public String Delete(Long id) {
        agendamentoRepository.deleteById(id);
        return "Agendamento(" + id + ")" + " foi excluíddo!";
    }

}
