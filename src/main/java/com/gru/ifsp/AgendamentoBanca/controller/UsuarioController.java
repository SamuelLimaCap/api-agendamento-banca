package com.gru.ifsp.AgendamentoBanca.controller;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.UsuarioRepository;
import com.gru.ifsp.AgendamentoBanca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository){
        this.usuarioService = usuarioService;
    }

    // Add
    @PostMapping(value = "/usuarios")
    public Usuario Post(@RequestBody Usuario params){
        return usuarioService.Post(params);
    }

    //Get
    @GetMapping(value = "/usuarios")
    public List<Usuario> Get(){
        return usuarioService.Get();
    }

    // Get By ID
    @GetMapping(value = "/usuarios/{id}")
    public Usuario Get(@PathVariable Long id){
        return usuarioService.Get(id);
    }

    // Update
    @PutMapping(value = "/usuarios/{id}")
    public Usuario Update(@PathVariable Long id, @RequestBody Usuario params){
        return usuarioService.atualizar(params, id);
    }

    //Delete
    @DeleteMapping(value = "/usuarios/{id}")
    public String Delete(@PathVariable Long id){
        return usuarioService.excluir(id);
    }
}
