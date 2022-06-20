package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoUsuariosForm;
import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.UsuarioParticipantesPorBanca;
import com.gru.ifsp.AgendamentoBanca.model.UsuariosParticipantesBancaPK;
import com.gru.ifsp.AgendamentoBanca.model.enums.StatusAgendamento;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.BancaNaoEncontradaException;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UsuarioNaoEncontradoException;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UsuarioNaoEncontradoNaBanca;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UsuariosParticipantesPorBancaRepository;
import com.gru.ifsp.AgendamentoBanca.services.contracts.EmailService;
import com.gru.ifsp.AgendamentoBanca.util.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


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

        BancaUtils.validateIfHasBancaCloseToThisTime(form.getDataAgendamento(), form.getId(), agendamentoRepository);

        var participants = getListUsuarioByListID(form.getListaIdParticipantes());
        var measurers = getListUsuarioByListID(form.getListaIdAvaliadores());
        var measurersWhoWillBeAdmin = List.of(form.getAdminsBanca());
      
        BancaUtils.validateIfOneAdminIsNotAtLeastMeasurer(measurersWhoWillBeAdmin, measurers);

        var bancaCreated = createBanca(form, participants, measurers);
        addMembersOnBanca(bancaCreated, participants, false);
        addMembersOnBanca(bancaCreated, measurers, true);
        addAdminBancaRoleToMembers(bancaCreated, measurersWhoWillBeAdmin);

        sendEmailToParticipantsAndMeasurers(bancaCreated, participants, measurers);

        return bancaCreated;
    }

    private AgendamentoBanca createBanca(AgendamentoBancaForm form, List<Usuario> participants, List<Usuario> measurers) {
        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(form, participants, measurers);
        return agendamentoRepository.save(agendamentoBanca);
    }

    private void addMembersOnBanca(AgendamentoBanca banca, List<Usuario> usuarios, boolean isTeacher) {
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
                isStudent,
                false);

        usuariosParticipantesPorBancaRepository.save(usuariosParticipantesBanca);
    }


    private void addAdminBancaRoleToMembers(AgendamentoBanca banca, List<Long> adminIdList) {
        var professors = usuariosParticipantesPorBancaRepository.findAllByBancaIsAndIsTeacher(banca, true);

        var usersDesignedToBeAdmin = professors.stream()
                .filter(
                        professorOnBanca -> adminIdList.contains(professorOnBanca.getId().getUsuarioId())
                ).collect(Collectors.toList());
      
        if (usersDesignedToBeAdmin.isEmpty())
            throw new RuntimeException("Não tem usuários permitidos para administrarem a banca dentro da lista de admins enviada");

        usersDesignedToBeAdmin.forEach(professor -> professor.setIsAdmin(true));
        usuariosParticipantesPorBancaRepository.saveAll(usersDesignedToBeAdmin);
    }


    private void sendEmailToParticipantsAndMeasurers(AgendamentoBanca banca, List<Usuario> participants, List<Usuario> measurers) {
        if (Constants.actualState.equalsIgnoreCase(Constants.productionState)) {
            sendEmailToEveryUserToConfirmBanca(participants, banca.getId());
            sendEmailToEveryUserToConfirmBanca(participants, banca.getId());
        }
    }

    private void sendEmailToEveryUserToConfirmBanca(List<Usuario> userList, Long bancaId) {
        if (Constants.actualState.equalsIgnoreCase(Constants.productionState)) {
            userList.forEach(user ->
                    emailService.sendEmailToConfirmChangesOnBanca(user.getEmail(), bancaId, user.getId())
            );
        }
    }

    @Override
    public List<AgendamentoUsuariosForm> getAll() {
        var allBancas = agendamentoRepository.findAll();
        return bancaListFormatted(allBancas);
    }

    private List<AgendamentoUsuariosForm> bancaListFormatted(List<AgendamentoBanca> allBancas) {
        List<AgendamentoUsuariosForm> listOfAllBancas = new ArrayList<>();
        for (var banca : allBancas) {
            var objeto = getBancaAndBancaMembersByBancaId(banca.getId());
            listOfAllBancas.add(objeto);
        }
        return listOfAllBancas;
    }

    @Override
    public AgendamentoBanca getById(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(BancaNaoEncontradaException::new);
    }

    @Override
    public void updateUserForAdmin(Long idBanca, Long idUsuario, boolean permission) {
        var banca = getById(idBanca);
        var usuario = userRepository.findById(idUsuario).orElseThrow(UsuarioNaoEncontradoNaBanca::new);
        var usarioOnBanca = usuariosParticipantesPorBancaRepository
                .findByBancaAndUsuario(banca, usuario).get();

        usarioOnBanca.setIsAdmin(permission);
        usuariosParticipantesPorBancaRepository.save(usarioOnBanca);
    }

    @Override
    public AgendamentoUsuariosForm getBancaAndBancaMembersByBancaId(Long id) {
        var banca = getById(id);

        var bancaMembers = usuariosParticipantesPorBancaRepository.getByIdAgendamentoBancaId(id);
        var membersSegmentedByBancaRoles = UsuarioParticipantesPorBancaUtils.splitIntoBancaRoles(bancaMembers, banca.getId());

        var bancaListMembersForm = new AgendamentoUsuariosForm(
                banca,
                membersSegmentedByBancaRoles.get("alunos"),
                membersSegmentedByBancaRoles.get("professores"),
                membersSegmentedByBancaRoles.get("administradores"));

        return bancaListMembersForm;
    }


    @Override
    public AgendamentoBancaForm update(AgendamentoBancaForm bancaForm) {

        AgendamentoBanca banca = agendamentoRepository.findById(bancaForm.getId())
                .orElseThrow(BancaNaoEncontradaException::new);

        AgendamentoBanca oldBanca = banca.clone();
        setOldMembersOnOldBanca(oldBanca, banca);

        var newListParticipants = getListUsuarioByListID(bancaForm.getListaIdParticipantes());
        var newListMeasurers = getListUsuarioByListID(bancaForm.getListaIdAvaliadores());

        BancaUtils.validateIfHasBancaCloseToThisTime(bancaForm.getDataAgendamento(), bancaForm.getId(), agendamentoRepository);

        BancaUtils.validateIfOneAdminIsNotAtLeastMeasurer(List.of(bancaForm.getAdminsBanca()), newListMeasurers);

        updateMembersOnBanca(banca, newListParticipants, newListMeasurers);

        var bancaWithInformationUpdated = AgendamentoBancaUtils.convertFormToAgendamentoBanca(bancaForm, newListParticipants, newListMeasurers);
        var bancaUpdated = agendamentoRepository.save(bancaWithInformationUpdated);


        var bancaMembersListWithWaitingStatus = usuariosParticipantesPorBancaRepository.
                findAllByIdAgendamentoBancaIdAndStatusAgendamentoEquals(banca.getId(), StatusAgendamento.AGUARDANDO);

        if (bancaMembersListWithWaitingStatus.isPresent()) {
            bancaUpdated.setAgendamento(StatusAgendamento.AGUARDANDO);
            agendamentoRepository.save(bancaUpdated);
        }
  
        bancaWithInformationUpdated = bancaUpdated.clone();
        sendDifferencesBetweenBancas(oldBanca, bancaWithInformationUpdated);

        bancaMembersListWithWaitingStatus.ifPresent(usuarioParticipantesPorBancas ->
                sendEmailToEveryUserToConfirmBanca(
                        usuarioParticipantesPorBancas.stream().map(UsuarioParticipantesPorBanca::getUsuario).collect(Collectors.toList()),
                        banca.getId()
                )
        );
        return bancaForm;
    }

    private void setOldMembersOnOldBanca(AgendamentoBanca oldBanca, AgendamentoBanca banca) {
        oldBanca.setParticipantes(new ArrayList<>(banca.getParticipantes()));
        oldBanca.setAvaliadores(new ArrayList<>(banca.getAvaliadores()));
    }

    private void updateMembersOnBanca(AgendamentoBanca banca, List<Usuario> newListParticipants, List<Usuario> newListMeasurers) {
        var bancaActualMembers = usuariosParticipantesPorBancaRepository.getByIdAgendamentoBancaId(banca.getId());

        deleteNecessaryMembersOnBanca(bancaActualMembers, newListParticipants, newListMeasurers);
        addNewMembersOnBanca(banca, bancaActualMembers, newListParticipants, newListMeasurers);

    }

    private void deleteNecessaryMembersOnBanca(List<UsuarioParticipantesPorBanca> bancaActualMembers, List<Usuario> newListParticipants, List<Usuario> newListMeasurers) {
        var membersToBeDeletedOnBanca = bancaActualMembers.stream().filter(member ->
                !newListParticipants.contains(member.getUsuario())
                        &&
                        !newListMeasurers.contains(member.getUsuario())
        );
        membersToBeDeletedOnBanca.forEach(
                usuariosParticipantesPorBancaRepository::delete
        );
    }

    private void addNewMembersOnBanca(AgendamentoBanca banca,
                                      List<UsuarioParticipantesPorBanca> bancaActualMembers,
                                      List<Usuario> newListParticipants,
                                      List<Usuario> newListMeasurers) {
        var bancaActualMembersUserClass = bancaActualMembers.stream().map(x -> x.getUsuario()).collect(Collectors.toList());

        var participantsToAdd = newListParticipants.stream()
                .filter(participant ->
                        !bancaActualMembersUserClass.contains(participant)
                ).collect(Collectors.toList());

        var measurersToAdd = newListMeasurers.stream().
                filter(measurer ->
                        !bancaActualMembersUserClass.contains(measurer))
                .collect(Collectors.toList());


        addMembersOnBanca(banca, participantsToAdd, false);
        addMembersOnBanca(banca, measurersToAdd, true);
    }




    private void sendDifferencesBetweenBancas(AgendamentoBanca oldBanca, AgendamentoBanca bancaWithInformationUpdated) {
        emailService.sendDifferencesBetweenNewAndOldBancaUpdate(oldBanca, bancaWithInformationUpdated);
    }

    @Override
    public String delete(Long id) {
        var banca = this.getById(id);
        deleteAllUsersInBanca(banca);
        agendamentoRepository.deleteById(id);
        return "Agendamento(" + id + ")" + " foi excluíddo!";
    }

    @Override
    public boolean setSubscriptionStatus(Long bancaId, Long userId, StatusAgendamento newStatus) {
        var result =
                usuariosParticipantesPorBancaRepository.findById(new UsuariosParticipantesBancaPK(bancaId, userId));

        if (result.isEmpty())
            throw new RuntimeException("Nao tem esse usuario nessa banca ou essa banca nao está cadastrada");

        var userOnBanca = result.get();
        setUserSubscriptionStatus(userOnBanca, newStatus);

        var usersStillToConfirm = getUsersThatStillWaitingConfirmation(bancaId);
        var usersCancelled = getUsersThatCancelled(bancaId);
        var hasSomeProblemToConfirmBanca = usersStillToConfirm.isPresent() | usersCancelled.isPresent();

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

    @Override
    public AgendamentoBancaForm addParticipantes(AgendamentoBancaForm bancaForm) {

        var alunosIDs = bancaForm.getListaIdParticipantes();
        var professoresIDs = bancaForm.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

        AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(bancaForm, listaAlunos, listaProfessores);

        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addMembersOnBanca(agendamentoBanca, listaAlunos, false);
        addMembersOnBanca(agendamentoBanca, listaProfessores, true);

        return bancaForm;
    }

}
