package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.annotations.IsAdminOrCoordenator;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.ProntuarioAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.form.UserActivationForm;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.UserNotExistException;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.response.UsuarioResponse;
import com.gru.ifsp.AgendamentoBanca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UsuarioForm form) {
        try {

            var dadosParaAtivacaoResponse = usuarioService.createUser(form);

            return ResponserHandler.generateResponse("Usuário cadastrado com sucesso!", HttpStatus.OK, dadosParaAtivacaoResponse);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();

            List<String> violations = e.getConstraintViolations().stream()
                    .map(constraintViolation ->
                            "Campo mal formado: " + constraintViolation.getPropertyPath().toString())
                    .collect(Collectors.toList());
            return ResponserHandler.generateResponse(violations.stream().reduce("\n", String::concat), HttpStatus.BAD_REQUEST, null);
        } catch (EmailAlreadyExists e) {
            return ResponserHandler.generateResponse("Email já foi cadastrado anteriormente!", HttpStatus.CONFLICT, null);
        } catch (ProntuarioAlreadyExists e) {
            return ResponserHandler.generateResponse("Prontuário já foi cadastrado anteriormente!", HttpStatus.CONFLICT, null);
        }
    }


    @DeleteMapping("/disable/{email}")
    @IsAdminOrCoordenator
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        try {
            if (email.isEmpty()) throw new UserNotExistException("Este usuário não existe");
            usuarioService.disableUser(email);

            return ResponserHandler.generateResponse("Usuário removido com sucesso!", HttpStatus.OK, null);
        } catch (UserNotExistException e) {
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> listUsers() {
        List<UsuarioResponse> usuarioReponseList = usuarioService.listUsers();
        return ResponserHandler.generateResponse("usuarios retornados com sucesso", HttpStatus.OK, usuarioReponseList);
    }

    @PostMapping("/activateUser")
    public ResponseEntity<Object> activateUser(@RequestBody UserActivationForm form) {
        try {
            usuarioService.activateUser(form);
            return ResponserHandler.generateResponse("Usuário ativado com sucesso!!", HttpStatus.OK, null);
        } catch (RuntimeException e) {
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/resendActivationCode")
    public ResponseEntity<Object> resendCode(@RequestBody String email) {
        try {
            usuarioService.resendActivationCode(email.toLowerCase(Locale.ROOT));
            return ResponserHandler.generateResponse("Código reenviado com sucesso!!", HttpStatus.OK, null);
        } catch (RuntimeException e) {
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/needAccountActivation")
    @IsAdminOrCoordenator
    public ResponseEntity<Object> needAcountActivation() {
        var response = usuarioService.getListOfUsersThatDoesntActivateAccount();
        return ResponserHandler.generateResponse("Dados retornados com sucesso!!", HttpStatus.OK, response);
    }
}
