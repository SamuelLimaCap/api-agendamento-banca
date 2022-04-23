package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.entity.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.services.AgendamentoBancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {


    private final AgendamentoBancaService agendamentoService;

    public AgendamentoController(AgendamentoBancaService agendamentoService) {
        this.agendamentoService = agendamentoService;

    }

//    Add
    @PostMapping
    public ResponseEntity<Object> Post(@RequestBody AgendamentoBanca parametros){
        try{
            AgendamentoBanca resultado = agendamentoService.Post(parametros);
            return ResponserHandler.generateResponse("Succeso ao adicionar a banca!", HttpStatus.OK, resultado);
        } catch (Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

//    Get
    @GetMapping
    public ResponseEntity<Object> Get(){
        try{
            List<AgendamentoBanca> resultado =  agendamentoService.Get();
            return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);

        } catch (Exception e) {
            return  ResponserHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

//    Get By ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> Get(@PathVariable Long id){
        try{
            AgendamentoBanca resultado = agendamentoService.Get(id);
            return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
        } catch (Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

//    Update
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> Update(@PathVariable Long id, @RequestBody AgendamentoBanca parametros){
       try{
           AgendamentoBanca resultado = agendamentoService.Update(parametros, id);
           return ResponserHandler.generateResponse("Atualizado!", HttpStatus.OK, resultado);
       } catch (Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
       }
    }

//    Delete
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> Delete(@PathVariable Long id){
        try{
            String resultado = agendamentoService.Delete(id);
            return ResponserHandler.generateResponse("Excluído!", HttpStatus.OK, resultado);
        } catch( Exception e){
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }



}
