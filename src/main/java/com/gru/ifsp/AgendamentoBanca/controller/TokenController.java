package com.gru.ifsp.AgendamentoBanca.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.util.JwtUtil;
import com.gru.ifsp.AgendamentoBanca.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.PREFIX_AUTHORIZATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private UserRepository userRepository;

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

}
