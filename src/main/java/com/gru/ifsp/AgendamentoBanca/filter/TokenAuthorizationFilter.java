package com.gru.ifsp.AgendamentoBanca.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gru.ifsp.AgendamentoBanca.util.JwtUtil;
import com.gru.ifsp.AgendamentoBanca.util.ResponseUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.PREFIX_AUTHORIZATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (servletPath.equals("/login") || servletPath.equals("/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(PREFIX_AUTHORIZATION)) {
                try {
                    checkAndAuthenticateUser(authorizationHeader);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    System.out.println("Error on doFilterInternal: " + e.getMessage());
                    ResponseUtils.showErrorOnResponse(response, FORBIDDEN, e.getMessage());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private void checkAndAuthenticateUser(String authorizationHeader) throws JWTVerificationException {
        DecodedJWT decodedJWT = JwtUtil.checkAndGetDecodedJWT(authorizationHeader);
        String email = decodedJWT.getSubject();
        authenticateUser(email);
    }

    private void authenticateUser(String email) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
