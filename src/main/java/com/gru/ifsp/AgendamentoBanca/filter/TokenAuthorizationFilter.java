package com.gru.ifsp.AgendamentoBanca.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gru.ifsp.AgendamentoBanca.util.Constants;
import com.gru.ifsp.AgendamentoBanca.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.PREFIX_AUTHORIZATION;
import static com.gru.ifsp.AgendamentoBanca.util.Constants.SECRET;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(PREFIX_AUTHORIZATION)) {
                //Tem alguma coisa no header "Authorization" que starta com o prefix definido
                try {
                    String accessToken = authorizationHeader.substring(PREFIX_AUTHORIZATION.length());
                    Algorithm algorithm = JwtUtil.signatureAlgorithm;

                    //Verifica se o token foi feito corretamente.
                    //Caso encontre alguma falha, jogará uma exception
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(accessToken);

                    //Pega os dados do token decodado
                    String email = decodedJWT.getSubject();
                    //Faz a autenticação do usuário
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    System.out.println("foi");
                    filterChain.doFilter(request, response);

                } catch (Exception e) {
                    // Usuario nao foi autorizado, irá cair aqui
                    System.out.println("Error on doFilterInternal: " + e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());

                    Map<String, String> responseMap = new HashMap<>();
                    responseMap.put("error", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), responseMap);
                }
            } else {
                System.out.println("foi");
                filterChain.doFilter(request, response);

            }
        }



    }
}
