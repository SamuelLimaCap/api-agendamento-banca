package com.gru.ifsp.AgendamentoBanca.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.services.UserServiceImpl;
import com.gru.ifsp.AgendamentoBanca.util.Constants;
import com.gru.ifsp.AgendamentoBanca.util.JwtUtil;
import com.gru.ifsp.AgendamentoBanca.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.PREFIX_AUTHORIZATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    private final UserServiceImpl userService;

    public TokenAuthorizationFilter(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (servletPath.startsWith(Constants.AUTH_ROUTE)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeaderIsValid(authorizationHeader)) {
                try {
                    DecodedJWT decodedJWT = JwtUtil.checkAndGetDecodedJWT(authorizationHeader);
                    String email = decodedJWT.getSubject();
                    authenticateUser(email);
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

    private boolean authorizationHeaderIsValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(PREFIX_AUTHORIZATION);
    }

    private void authenticateUser(String email) {
        Usuario dbUser = getUserFromDB(email);
        List<SimpleGrantedAuthority> authorities = dbUser.getPermissaoList().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getCodeName())).collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dbUser, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private Usuario getUserFromDB(String email) {
        Usuario usuario = userService.getUsuario(email);
        if (usuario == null || !usuario.isEnabled()) throw new UsernameNotFoundException("User not found");

        return usuario;
    }
}
