package com.gru.ifsp.AgendamentoBanca.entity.springsecurity.filter;

import com.gru.ifsp.AgendamentoBanca.model.Permissao;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.springsecurity.AuthUser;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.util.JwtUtil;
import com.gru.ifsp.AgendamentoBanca.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final PermissaoRepository permissionRepository;

    private final UserRepository userRepository;

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager, PermissaoRepository permissionRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        AuthUser user = (AuthUser) authentication.getPrincipal();

        var userDb = userRepository.findByEmail(user.getUsername());
        List<Permissao> permissaoList = userDb.getPermissaoList();

        String currentUrl = request.getRequestURL().toString();

        String accessToken = JwtUtil.generateAccessToken(userDb, currentUrl);
        String refreshToken = JwtUtil.generateRefreshToken(userDb, currentUrl);

        ResponseUtils.showTokensOnResponse(response, accessToken, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtils.showErrorOnResponse( response, HttpStatus.UNAUTHORIZED, failed.getMessage());
    }
}
