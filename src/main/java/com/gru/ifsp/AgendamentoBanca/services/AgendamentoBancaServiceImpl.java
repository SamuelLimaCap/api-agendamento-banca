package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.dtos.UsuarioDto;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoUsuariosForm;
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
import com.gru.ifsp.AgendamentoBanca.services.contracts.EmailService;
import com.gru.ifsp.AgendamentoBanca.util.AgendamentoBancaUtils;
import com.gru.ifsp.AgendamentoBanca.util.Constants;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Transactional
@Service
public class AgendamentoBancaServiceImpl implements AgendamentoBancaService {

    private final AgendamentoRepository agendamentoRepository;

    private final UserRepository userRepository;
    private final UsuariosParticipantesPorBancaRepository usuariosParticipantesPorBancaRepository;

    private final EmailService emailService;

    public AgendamentoBancaServiceImpl(AgendamentoRepository agendamentoRepository, UserRepository userRepository, UsuariosParticipantesPorBancaRepository usuariosParticipantesPorBancaRepository, EmailService emailService) {
        this.agendamentoRepository = agendamentoRepository;
        this.userRepository = userRepository;
        this.usuariosParticipantesPorBancaRepository = usuariosParticipantesPorBancaRepository;
        this.emailService = emailService;
    }

    @Override
    public AgendamentoBanca add(AgendamentoBancaForm form) {

        var alunosIDs = form.getListaIdParticipantes();
        var professoresIDs = form.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        checkyIfCanCreateAgendamentoOnThisTime(form.getDataAgendamento());

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(form, listaAlunos, listaProfessores);

        var bancaSalva = agendamentoRepository.save(agendamentoBanca);
        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(agendamentoBanca, listaAlunos, false);
        addUsuariosOnBanca(agendamentoBanca, listaProfessores, true);

        if (Constants.actualState.equalsIgnoreCase(Constants.productionState)) {
            sendEmailToEveryUserToConfirmBanca(listaAlunos, bancaSalva.getId());
            sendEmailToEveryUserToConfirmBanca(listaProfessores, bancaSalva.getId());
        }

        return agendamentoBanca;
    }

    private void checkyIfCanCreateAgendamentoOnThisTime(String dataAgendamento) throws RuntimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var data = LocalDateTime.parse(dataAgendamento, formatter);


        var horarios = agendamentoRepository
                .findByDataAgendamentoBetween(data.minusMinutes(50),
                        data.plusMinutes(50));

        if (horarios.size() > 0) {
            throw new RuntimeException("Banca não pode ser cadastrada neste horário");
        }
    }


    private void sendEmailToEveryUserToConfirmBanca(List<Usuario> userList, Long bancaId) {
        userList.forEach(user ->
                emailService.sendEmailToConfirmChangesOnBanca(user.getEmail(), bancaId, user.getId())
        );
    }

    @Override
    public List<AgendamentoUsuariosForm> getAll() {
        var allBancas = agendamentoRepository.findAll();
        return listOfBancas(allBancas);
    }

    private List<AgendamentoUsuariosForm> listOfBancas(List<AgendamentoBanca> allBancas) {
        List<AgendamentoUsuariosForm> listOfAllBancas = new ArrayList<>();
        for (var banca : allBancas) {
            var objeto = getBancaAndUsuariosByBancaId(banca.getId());
            listOfAllBancas.add(objeto);
        }
        return listOfAllBancas;
    }

    @Override
    public AgendamentoBanca getById(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(BancaNaoEncontradaException::new);
    }

    @Override
    public AgendamentoUsuariosForm getBancaAndUsuariosByBancaId(Long id) {
        var banca = getById(id);

        var usuariosParticipantes = usuariosParticipantesPorBancaRepository
                .returAllMembersOnBanca(id);


        var listaParticipantes = splitIntoMapOfProfessorsAndStudents
                (usuariosParticipantes, banca.getId());

        var bancaUsuariosForm = new AgendamentoUsuariosForm(banca,
                listaParticipantes.get("alunos"), listaParticipantes.get("professores"));

        return bancaUsuariosForm;
    }

    public Map<String, List<UsuarioDto>> splitIntoMapOfProfessorsAndStudents(Long[] alunosProfessores, Long idBanca) {
        List<UsuarioDto> alunos = new ArrayList<>();
        List<UsuarioDto> professores = new ArrayList<>();

        for (var usuarioId : alunosProfessores) {
            var user = getUsuarioByID(usuarioId);
            if (usuariosParticipantesPorBancaRepository
                    .verifyUserIsTeacher(user.getId(), idBanca)) {
                professores.add(new UsuarioDto(user));
            } else
                alunos.add(new UsuarioDto(user));
        }


        Map<String, List<UsuarioDto>> alunosProfesoresSeparados = new HashMap<>();
        alunosProfesoresSeparados.put("alunos", alunos);
        alunosProfesoresSeparados.put("professores", professores);

        return alunosProfesoresSeparados;
    }


    @Override
    public AgendamentoBancaForm update(AgendamentoBancaForm bancaForm) {

        AgendamentoBanca banca = agendamentoRepository.findById(bancaForm.getId())
                .orElseThrow(BancaNaoEncontradaException::new);

        AgendamentoBanca oldBanca = banca.clone();
        oldBanca.setParticipantes(new ArrayList<>(banca.getParticipantes()));

        var dataAgendamentoAtualizada = LocalDateTime
                .parse(bancaForm.getDataAgendamento(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        var alunosIDs = bancaForm.getListaIdParticipantes();
        var professoresIDs = bancaForm.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        checkyIfCanCreateAgendamentoOnThisTime(bancaForm.getDataAgendamento());
        AgendamentoBanca bancaAtualizada = AgendamentoBancaUtils.convertFormToAgendamentoBanca(bancaForm, listaAlunos, listaProfessores);
        //Delete all users in Banca
        deleteAllUsersInBanca(banca);
        //Add a new lis of users in Banca
        var bancaFormAtualizada = addParticipantes(bancaForm);

        var bancaSalva = agendamentoRepository.save(bancaAtualizada);
        bancaAtualizada = bancaSalva.clone();
        boolean imprimiu = emailService.sendDifferencesBetweenNewAndOldBancaUpdate(oldBanca, bancaAtualizada);


        System.out.println("imprimiu: " + imprimiu);
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
    public AgendamentoBancaForm addParticipantes(AgendamentoBancaForm bancaForm) {

        var alunosIDs = bancaForm.getListaIdParticipantes();
        var professoresIDs = bancaForm.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        checkyIfCanCreateAgendamentoOnThisTime(bancaForm.getDataAgendamento());

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(bancaForm, listaAlunos, listaProfessores);

        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(agendamentoBanca, listaAlunos, false);
        addUsuariosOnBanca(agendamentoBanca, listaProfessores, true);

        return bancaForm;
    }

    @Override
    public boolean setSubscriptionStatus(Long bancaId, Long userId, StatusAgendamento statusAgendamento) {
        var result =
                usuariosParticipantesPorBancaRepository.findById(new UsuariosParticipantesBancaPK(bancaId, userId));

        if (result.isEmpty())
            throw new RuntimeException("Nao tem esse usuario nessa banca ou essa banca nao está cadastrada");

        var userOnBanca = result.get();
        setUserSubscriptionStatus(userOnBanca, statusAgendamento);

        var usersThatStillWaitingConfirmation = getUsersThatStillWaitingConfirmation(bancaId);
        var usersCanceledConfirmation = getUsersThatCancelled(bancaId);
        var hasSomeProblemToConfirmBanca = usersThatStillWaitingConfirmation.isPresent() | usersCanceledConfirmation.isPresent();

        if (hasSomeProblemToConfirmBanca) {
            //TODO send email for everyone showing that one person confirmed subscription and are missing theses members
        } else {
            var banca = agendamentoRepository.getById(bancaId);
            setBancaToConfirmedStatus(banca);
            sendToUsersOfBancaThatStatusIsConfirmed(banca);
        }

        return false;
    }

    private void setUserSubscriptionStatus(UsuarioParticipantesPorBanca userOnBanca, StatusAgendamento statusAgendamento) {
        userOnBanca.setStatusAgendamento(statusAgendamento);
        usuariosParticipantesPorBancaRepository.save(userOnBanca);
    }

    private Optional<List<UsuarioParticipantesPorBanca>> getUsersThatStillWaitingConfirmation(Long bancaId) {
        return usuariosParticipantesPorBancaRepository
                .findAllByIdAgendamentoBancaIdAndStatusAgendamentoEquals(bancaId, StatusAgendamento.AGUARDANDO);
    }

    private Optional<List<UsuarioParticipantesPorBanca>> getUsersThatCancelled(Long bancaId) {
        return usuariosParticipantesPorBancaRepository
                .findAllByIdAgendamentoBancaIdAndStatusAgendamentoEquals(bancaId, StatusAgendamento.CANCELADO);
    }

    private void setBancaToConfirmedStatus(AgendamentoBanca banca) {
        banca.setAgendamento(StatusAgendamento.AGENDADO);
        agendamentoRepository.save(banca);
    }

    private void sendToUsersOfBancaThatStatusIsConfirmed(AgendamentoBanca banca) {
        var usersOnBanca = new ArrayList<Usuario>();
        usersOnBanca.addAll(banca.getParticipantes());
        usersOnBanca.addAll(banca.getAvaliadores());

        if (Constants.actualState.equalsIgnoreCase(Constants.productionState)) {
            usersOnBanca.forEach(user -> {
                emailService.sendCustomMessageEmail(
                        "Atualização Banca" + banca.getTitulo(),
                        "Todos os participantes aceitaram a inscrição para a banca",
                        "Caso não ocorra nenhuma alteração, tudo ocorrerá conforme descrito nessa banca",
                        user.getEmail()
                );
            });
        }
    }

    private void addUsuariosOnBanca(AgendamentoBanca banca, List<Usuario> usuarios, boolean isTeacher) {
        for (Usuario usuario : usuarios) {
            addUserOnMembersOfBanca(banca, usuario, isTeacher);
        }
    }

    private void addUserOnMembersOfBanca(AgendamentoBanca banca, Usuario usuario, boolean isTeacher) {
        boolean isStudent = !isTeacher;
        var usuariosParticipantesBanca = new UsuarioParticipantesPorBanca(
                new UsuariosParticipantesBancaPK(
                        banca.getId(),
                        usuario.getId()
                ),
                banca,
                usuario,
                StatusAgendamento.AGUARDANDO,
                isTeacher,
                isStudent);

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


    private void deleteAllUsersInBanca(AgendamentoBanca banca) {
        usuariosParticipantesPorBancaRepository.deleteAllByBancaIs(banca);
    }

}
