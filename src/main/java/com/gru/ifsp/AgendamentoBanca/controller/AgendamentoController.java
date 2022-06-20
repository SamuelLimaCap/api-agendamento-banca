package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.enums.PermissaoEnum;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.services.AgendamentoBancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoBancaService agendamentoService;


    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody AgendamentoBancaForm agendamentoBancaForm) throws Exception {
            AgendamentoBanca resultado = agendamentoService.add(agendamentoBancaForm);
            return ResponserHandler.generateResponse("Succeso ao adicionar a banca!", HttpStatus.CREATED, agendamentoBancaForm);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.USUARIO+"')")
    @GetMapping
    public ResponseEntity<Object> getAll(){
        var resultado =  agendamentoService.getAll();
        return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.USUARIO+"')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        var resultado = agendamentoService.getBancaAndUsuariosByBancaId(id);
        return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.USUARIO+"')")
    @PutMapping
    public ResponseEntity<Object> update(@RequestBody AgendamentoBancaForm bancaForm){
       var resultado = agendamentoService.update(bancaForm);
       return ResponserHandler.generateResponse("Atualizado!", HttpStatus.OK, resultado);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        String resultado = agendamentoService.delete(id);
        return ResponserHandler.generateResponse("Excluído!", HttpStatus.OK, resultado);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @PostMapping(value = "/add-participantes")
    public ResponseEntity<Object> addParticipante(@RequestBody AgendamentoBancaForm banca){
        agendamentoService.addParticipantes(banca);
        return ResponserHandler.generateResponse("Participantes incluídos com sucesso!", HttpStatus.OK, banca);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @PostMapping("add-admin/{idBanca}/{idUsuario}")
    public ResponseEntity<Object> addAdmin(@PathVariable("idBanca") Long idBanca, @PathVariable("idUsuario") Long idUsuario){
        agendamentoService.updateUserForAdmin(idBanca, idUsuario, true);
        return ResponserHandler.generateResponse("Participante definido como administrador da banca!", HttpStatus.OK,null);
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @PostMapping("delete-admin/{idBanca}/{idUsuario}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable("idBanca") Long idBanca, @PathVariable("idUsuario") Long idUsuario){
        agendamentoService.updateUserForAdmin(idBanca, idUsuario, false);
        return ResponserHandler.generateResponse("Participante excluído como administrador da banca!", HttpStatus.OK,null);
    }
}
