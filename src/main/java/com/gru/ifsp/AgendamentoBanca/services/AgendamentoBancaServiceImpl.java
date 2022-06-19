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
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UsuarioNaoEncontradoNaBanca;
import com.gru.ifsp.AgendamentoBanca.repositories.AgendamentoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UsuariosParticipantesPorBancaRepository;
import com.gru.ifsp.AgendamentoBanca.util.AgendamentoBancaUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        checkyIfCanCreateAgendamentoOnThisTime(form.getDataAgendamento());

        var alunosIDs = form.getListaIdParticipantes();
        var professoresIDs = form.getListaIdAvaliadores();

        var listaAlunos = getListUsuarioByListID(alunosIDs);
        var listaProfessores = getListUsuarioByListID(professoresIDs);

       AgendamentoBanca agendamentoBanca = AgendamentoBancaUtils.convertFormToAgendamentoBanca(form, listaAlunos, listaProfessores);

        agendamentoRepository.save(agendamentoBanca);
        //Add users on banca through the entity responsible for assert relationship beetween banca and users
        addUsuariosOnBanca(agendamentoBanca, listaAlunos, false);
        addUsuariosOnBanca(agendamentoBanca,listaProfessores, true);
        //Set who is admin of banca
        setAdminsOfBanca(agendamentoBanca,form.getAdminsBanca());
        return agendamentoBanca;
    }

    private void setAdminsOfBanca(AgendamentoBanca banca, Long[] adminsBanca) {
        List<UsuarioParticipantesPorBanca> listaProfessores = usuariosParticipantesPorBancaRepository.findAllByBancaIsAndIsTeacher(banca, true);
        if(listaProfessores.size() > 0){
            listaProfessores.forEach(professor -> Arrays.stream(adminsBanca).forEach(id -> {
                if (Objects.equals(id, professor.getUsuario().getId())) {
                    professor.setIsAdmin(true);
                }
            }));
            usuariosParticipantesPorBancaRepository.saveAll(listaProfessores);
        } else{
            throw new RuntimeException("Não há usuários permitidos para admistrar bancas");
        }
    }

    public void updateUserForAdmin(Long idBanca, Long idUsuario, boolean permission){
        var banca = getById(idBanca);
        var usuario = userRepository.findById(idUsuario).orElseThrow(UsuarioNaoEncontradoNaBanca::new);
        var usarioOnBanca = usuariosParticipantesPorBancaRepository
                .findByBancaAndUsuario(banca, usuario).get();

        usarioOnBanca.setIsAdmin(permission);
        usuariosParticipantesPorBancaRepository.save(usarioOnBanca);
    }

    private LocalDateTime DateStringToLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    private void checkyIfCanCreateAgendamentoOnThisTime(String dataAgendamento) throws RuntimeException {
        var data = DateStringToLocalDateTime(dataAgendamento);

        var horarios = agendamentoRepository
                .findByDataAgendamentoBetween(data.minusMinutes(50),
                        data.plusMinutes(50));

        if(horarios.size() > 0){
           for(var horario : horarios){
               if(!(data == horario.getDataAgendamento()) && horarios.size() > 1){
                   throw new RuntimeException("Banca não pode ser cadastrada neste horário");
               }
           }
        }
    }

    @Override
    public List<AgendamentoUsuariosForm> getAll() {
        var allBancas = agendamentoRepository.findAll();
        return listOfBancas(allBancas);
    }

    private List<AgendamentoUsuariosForm> listOfBancas(List<AgendamentoBanca> allBancas){
        List<AgendamentoUsuariosForm> listOfAllBancas = new ArrayList<>();
        for(var banca : allBancas){
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
    public AgendamentoUsuariosForm getBancaAndUsuariosByBancaId(Long id){
        var banca = getById(id);
        var usuariosParticipantes = usuariosParticipantesPorBancaRepository
                .returAllMembersOnBanca(id);


        var listaParticipantes = splitIntoMapOfProfessorsAndStudents
                (usuariosParticipantes, banca.getId());

        return new AgendamentoUsuariosForm(banca,
                listaParticipantes.get("alunos"), listaParticipantes.get("professores"), listaParticipantes.get("administradores"));
    }

    public Map<String, List<UsuarioDto>> splitIntoMapOfProfessorsAndStudents(Long[] alunosProfessores, Long idBanca){
        List<UsuarioDto> alunos = new ArrayList<>();
        List<UsuarioDto> professores = new ArrayList<>();
        var administradores = returnAllAdminsOfABanca(idBanca);

        for(var usuarioId : alunosProfessores){
            var user = getUsuarioByID(usuarioId);
            if(usuariosParticipantesPorBancaRepository
                    .verifyUserIsTeacher(user.getId(), idBanca)){
                professores.add(new UsuarioDto(user));

            } else
                alunos.add(new UsuarioDto(user));
        }

        Map<String, List<UsuarioDto>> alunosProfesoresSeparados = new HashMap<>();
        alunosProfesoresSeparados.put("alunos",alunos);
        alunosProfesoresSeparados.put("professores",professores);
        alunosProfesoresSeparados.put("administradores", administradores);

        return alunosProfesoresSeparados;
    }

    public List<UsuarioDto> returnAllAdminsOfABanca(Long idBanca){
        List<UsuarioDto> administradores = new ArrayList<>();
        var usersAdmins = usuariosParticipantesPorBancaRepository.findAllByIsAdmin(idBanca);
        for(var id : usersAdmins){
            var user = getUsuarioByID(id);
            administradores.add(new UsuarioDto(user));
        }
        return administradores;
    }


    @Override
    public AgendamentoBancaForm update(AgendamentoBancaForm bancaForm) {

        AgendamentoBanca agendamento = agendamentoRepository.findById(bancaForm.getId())
                .orElseThrow(BancaNaoEncontradaException::new);

        var dataAgendamentoAtualizada = DateStringToLocalDateTime(bancaForm.getDataAgendamento());

        //validate date agendamento
        checkyIfCanCreateAgendamentoOnThisTime(bancaForm.getDataAgendamento());

        agendamento.setTitulo(bancaForm.getTitulo());
        agendamento.setDescricao(bancaForm.getDescricao());
        agendamento.setTipoBanca(bancaForm.getTipoBanca());
        agendamento.setTema(bancaForm.getTema());
        agendamento.setDataAgendamento(dataAgendamentoAtualizada);
        agendamento.setAgendamento(bancaForm.getStatusAgendamento());
        //Delete all users in Banca
        deleteAllUsersInBanca(agendamento);
        //Add a new list of users in Banca
        var bancaFormAtualizada = addParticipantes(bancaForm);
        //update admins of a banca
        setAdminsOfBanca(agendamento, bancaForm.getAdminsBanca());
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
                banca, usuario, StatusAgendamento.AGUARDANDO, isTeacher, null);
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
