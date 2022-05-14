package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.UserNotExistException;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.response.UsuarioResponse;
import com.gru.ifsp.AgendamentoBanca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UsuarioForm form) {
        try {

            Usuario usuario = usuarioService.createUser(form);
            usuario.setPassword("");

            return ResponserHandler.generateResponse("Usuário cadastrado com sucesso!", HttpStatus.OK, usuario);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();

            List<String> violations = e.getConstraintViolations().stream()
                    .map(constraintViolation ->
                            "Campo mal formado:" + constraintViolation.getPropertyPath().toString())
                    .collect(Collectors.toList());
            return ResponserHandler.generateResponse(violations.stream().reduce("\n", String::concat), HttpStatus.BAD_REQUEST, null);
        } catch (EmailAlreadyExists e) {
            return ResponserHandler.generateResponse("Usuário já existe!", HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        try {
            if (email.isEmpty()) throw new UserNotExistException("Este usuário não existe");
            //TODO no momento está deletando o usuário. O correto seria desativa-lo
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
}
