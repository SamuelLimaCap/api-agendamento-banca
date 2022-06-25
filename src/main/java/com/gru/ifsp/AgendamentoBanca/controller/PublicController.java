package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.annotations.IsAlunoOrAbove;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.services.AgendamentoBancaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {

    private AgendamentoBancaService agendamentoBancaService;

    @GetMapping("/agendamentos")
    public ResponseEntity<Object> getAll() {
        var resultado = agendamentoBancaService.getAll();
        return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
    }


    @GetMapping(value = "/agendamentos/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        var resultado = agendamentoBancaService.getBancaAndBancaMembersByBancaId(id);
        return ResponserHandler.generateResponse("Sucesso ao retornar dados!", HttpStatus.OK, resultado);
    }

}
