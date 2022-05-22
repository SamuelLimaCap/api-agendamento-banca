package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.model.UsuariosParticipantesBancaPK;
import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.BancaNaoEncontradaException;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UsuarioNaoEncontradoException;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UsuariosParticipantesPorBancaRepository;
import com.gru.ifsp.AgendamentoBanca.util.AgendamentoBancaUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Transactional
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

        checkyIfCanCreateAgendamentoOnThisTime(form.getDataAgendamento());

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(form, listaAlunos, listaProfessores);

        agendamentoRepository.save(agendamentoBanca);
        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(agendamentoBanca, listaAlunos, false);
        addUsuariosOnBanca(agendamentoBanca,listaProfessores, true);

        return agendamentoBanca;
    }

    private void checkyIfCanCreateAgendamentoOnThisTime(String dataAgendamento) {
//        TODO
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
    public AgendamentoBancaForm update(AgendamentoBancaForm bancaForm, Long id) {

        AgendamentoBanca agendamento = agendamentoRepository.findById(id)
                .orElseThrow(BancaNaoEncontradaException::new);

        var dataAgendamentoAtualizada = LocalDateTime
                .parse(bancaForm.getDataAgendamento(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        agendamento.setTitulo(bancaForm.getTitulo());
        agendamento.setDescricao(bancaForm.getDescricao());
        agendamento.setTipoBanca(bancaForm.getTipoBanca());
        agendamento.setTema(bancaForm.getTema());
        agendamento.setDataAgendamento(dataAgendamentoAtualizada);
        agendamento.setAgendamento(bancaForm.getStatusAgendamento());
        //Delete all users in Banca
        deleteAllUsersInBanca(agendamento);
        //Add a new lis of users in Banca
        var bancaFormAtualizada =addParticipantes(bancaForm);
        agendamentoRepository.save(agendamento);
        return bancaFormAtualizada;
    }

    @Override
    public String delete(Long id) {
        var banca = this.getById(id);
        deleteAllUsersInBanca(banca);
        agendamentoRepository.deleteById(id);
        return "Agendamento(" + id + ")" + " foi excluíddo!";
    }

    @Override
    public AgendamentoBancaForm addParticipantes(AgendamentoBancaForm bancaForm){

        var alunosIDs = bancaForm.getListaIdParticipantes();
        var professoresIDs = bancaForm.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        checkyIfCanCreateAgendamentoOnThisTime(bancaForm.getDataAgendamento());

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(bancaForm, listaAlunos, listaProfessores);

        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(agendamentoBanca, listaAlunos, false);
        addUsuariosOnBanca(agendamentoBanca,listaProfessores, true);

        return bancaForm;
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


    private void deleteAllUsersInBanca(AgendamentoBanca banca){
        usuariosParticipantesPorBancaRepository.deleteAllByBancaIs(banca);
    }

}
