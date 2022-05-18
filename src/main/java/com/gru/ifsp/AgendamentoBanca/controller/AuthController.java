package com.gru.ifsp.AgendamentoBanca.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.ProntuarioAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.form.UserActivationForm;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.response.DadosParaAtivacaoResponse;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import com.gru.ifsp.AgendamentoBanca.services.UsuarioService;
import com.gru.ifsp.AgendamentoBanca.util.JwtUtil;
import com.gru.ifsp.AgendamentoBanca.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.PREFIX_AUTHORIZATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(PREFIX_AUTHORIZATION)) {
            try {
                String refreshToken = authorizationHeader.substring(PREFIX_AUTHORIZATION.length());
                DecodedJWT decodedJWT = JwtUtil.getDecodedJWTFromToken(refreshToken);
                String email = decodedJWT.getSubject();
                Usuario user = userRepository.findByEmail(email);
                String currentUrl = request.getRequestURL().toString();

                String accessToken = JwtUtil.generateAccessToken(user, currentUrl);
                ResponseUtils.showTokensOnResponse(response, accessToken, refreshToken);
            } catch (Exception e) {
                System.out.println("Error on refreshToken: " + e.getMessage());

                ResponseUtils.showErrorOnResponse(response, FORBIDDEN, e.getMessage());
            }
        }
        return ResponseEntity.ok("");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody UsuarioForm form) {
        try {

            DadosParaAtivacaoResponse usuario = usuarioService.createUser(form);

            return ResponserHandler.generateResponse("Usuário cadastrado com sucesso!", HttpStatus.OK, usuario);
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
    public ResponseEntity<Object> resendCode(String email) {
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

}
