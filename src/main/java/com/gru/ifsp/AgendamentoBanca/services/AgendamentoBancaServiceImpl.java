package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.model.UsuariosParticipantesBancaPK;
import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.BancaNaoEncontradaException;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UsuarioNaoEncontradoException;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UsuariosParticipantesPorBancaRepository;
import com.gru.ifsp.AgendamentoBanca.util.AgendamentoBancaUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoBancaServiceImpl implements AgendamentoBancaService {

    private final AgendamentoRepository agendamentoRepository;

    private final  UserRepository userRepository;
    private final UsuariosParticipantesPorBancaRepository usuariosParticipantesPorBancaRepository;

    public AgendamentoBancaServiceImpl(AgendamentoRepository agendamentoRepository, UserRepository userRepository, UsuariosParticipantesPorBancaRepository usuariosParticipantesPorBancaRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.userRepository = userRepository;
        this.usuariosParticipantesPorBancaRepository = usuariosParticipantesPorBancaRepository;
    }

    @Override
    public AgendamentoBanca add(AgendamentoBancaForm form){

        var alunosIDs = form.getListaIdParticipantes();
        var professoresIDs = form.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(form, listaAlunos, listaProfessores);

        agendamentoRepository.save(agendamentoBanca);
        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(agendamentoBanca, listaAlunos, false);
        addUsuariosOnBanca(agendamentoBanca,listaProfessores, true);

        return agendamentoBanca;
    }

    @Override
    public List<AgendamentoBanca> getAll() {
        return agendamentoRepository.findAll();
    }

    @Override
    public AgendamentoBanca getById(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(BancaNaoEncontradaException::new);
    }

    @Override
    public AgendamentoBanca update(AgendamentoBanca parametros, Long id) {

        AgendamentoBanca agendamento = agendamentoRepository.findById(id).orElseThrow(BancaNaoEncontradaException::new);
        agendamento.setTitulo(parametros.getTitulo());
        agendamento.setDescricao(parametros.getDescricao());
        agendamento.setTipoBanca(parametros.getTipoBanca());
        agendamento.setTema(parametros.getTema());
        agendamento.setDataAgendamento(parametros.getDataAgendamento());
        agendamento.setParticipantes(parametros.getParticipantes());
        agendamento.setAvaliadores(parametros.getAvaliadores());
        agendamento.setAgendamento(parametros.getAgendamento());

        return agendamentoRepository.save(agendamento);
    }

    @Override
    public String delete(Long id) {
        agendamentoRepository.deleteById(id);
        return "Agendamento(" + id + ")" + " foi excluíddo!";
    }

    @Override
    public AgendamentoBanca addParticipantes(AgendamentoBancaForm bancaForm){

        var banca = agendamentoRepository
                .findById(bancaForm.getId())
                .orElseThrow(BancaNaoEncontradaException::new);

        var alunosIDs = bancaForm.getListaIdParticipantes();
        var professoresIDs = bancaForm.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(banca, listaAlunos, false);
        addUsuariosOnBanca(banca,listaProfessores, true);

        //Refresh the current members of Banca only on banca class
        banca.setParticipantes(listaAlunos);
        banca.setAvaliadores(listaProfessores);

        agendamentoRepository.save(banca);

        var bancaAtualizada = AgendamentoBancaUtils
                .convertFormToAgendamentoBanca(bancaForm, listaAlunos, listaProfessores);

        return bancaAtualizada;
    }

    private void addUsuariosOnBanca(AgendamentoBanca banca, List<Usuario> usuarios, boolean isTeacher) {
        for(Usuario usuario : usuarios){
            addUserOnMembersOfBanca(banca, usuario, isTeacher);
        }
    }

    private void addUserOnMembersOfBanca(AgendamentoBanca banca, Usuario usuario, boolean isTeacher) {
        var usuariosParticipantesBanca = new UsuarioParticipantesPorBanca(
                new UsuariosParticipantesBancaPK(banca.getId(), usuario.getId()),
                banca, usuario, StatusAgendamento.AGUARDANDO, isTeacher);
        usuariosParticipantesPorBancaRepository.save(usuariosParticipantesBanca);
    }

    private List<Usuario> getListUsuarioByListID(Long[] listUsuarioID) {
        List<Usuario> usersList = new ArrayList<>();
        for (Long id : listUsuarioID) {
            Usuario usuario = getUsuarioByID(id);
            usersList.add(usuario);
        }
        return usersList;
    }

    private Usuario getUsuarioByID(Long usuarioId) {
        return userRepository
                .findById(usuarioId)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

}
