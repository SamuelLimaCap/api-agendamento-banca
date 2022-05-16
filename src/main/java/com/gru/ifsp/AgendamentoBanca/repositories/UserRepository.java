package com.gru.ifsp.AgendamentoBanca.repositories;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByProntuario(String prontuario);

}
