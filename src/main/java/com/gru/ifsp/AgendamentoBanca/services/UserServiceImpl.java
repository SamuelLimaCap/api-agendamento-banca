package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.entity.springsecurity.AuthUser;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = userRepository.findByEmail(email);
        if (user==null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is not activated");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new AuthUser(user.getEmail(), user.getPassword(), authorities, user.getId());

    }



    public Usuario getUsuario(String email) {
        return userRepository.findByEmail(email);
    }
}
