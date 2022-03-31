package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.form.LogarForm;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/save")
    private ResponseEntity<Boolean> salvar(@RequestBody LogarForm logarForm) {
        return ResponseEntity.ok(true);

    }

    @GetMapping("/list")
    private ResponseEntity<List<Usuario>> listar() {
        //Pega do repositório e omite as senhas
        List<Usuario> userList = userRepository.findAll().stream().map(user -> {
            user.setPassword("");
            return user;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(userList);
    }


}
