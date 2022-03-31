package com.gru.ifsp.AgendamentoBanca.repository;

import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
