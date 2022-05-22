package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.enums.PermissaoEnum;
import com.gru.ifsp.AgendamentoBanca.form.AgendamentoBancaForm;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.services.AgendamentoBancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        try{
            List<AgendamentoBanca> resultado =  agendamentoService.getAll();
            return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
        } catch (Exception e) {
            return  ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.USUARIO+"')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        try{
            AgendamentoBanca resultado = agendamentoService.getById(id);
            return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
        } catch (Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.USUARIO+"')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody AgendamentoBancaForm bancaForm){
       try{
           var resultado = agendamentoService.update(bancaForm, id);
           return ResponserHandler.generateResponse("Atualizado!", HttpStatus.OK, resultado);
       } catch (Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
       }
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        try{
            String resultado = agendamentoService.delete(id);
            return ResponserHandler.generateResponse("Excluído!", HttpStatus.OK, resultado);
        } catch( Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PreAuthorize("hasRole('"+ PermissaoEnum.Code.ADMIN+"')")
    @PostMapping(value = "/add-participantes")
    public ResponseEntity<Object> addParticipante(@RequestBody AgendamentoBancaForm banca){
        try{
            agendamentoService.addParticipantes(banca);
            return ResponserHandler.generateResponse("Participantes incluídos com sucesso!", HttpStatus.OK, banca);
        } catch( Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
