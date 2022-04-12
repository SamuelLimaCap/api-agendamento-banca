package com.gru.ifsp.AgendamentoBanca;

import com.gru.ifsp.AgendamentoBanca.entity.Permissao;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.entity.enums.PermissaoEnum;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class AgendamentoBancaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendamentoBancaApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository, PermissaoRepository permissaoRepository) {
        return args -> {

            permissaoRepository.saveAll(getAllPermissaoFromPermissaoEnum());
            Permissao permissaoAdmin = permissaoRepository.getByCodeName(PermissaoEnum.ADMIN.name());

            userRepository.save(
                    new Usuario(null, "admin@admin.com", passwordEncoder().encode("admin"),
                            true, List.of(permissaoAdmin)
                    )
            );
        };
    }

    private List<Permissao> getAllPermissaoFromPermissaoEnum() {
        return Arrays.stream(PermissaoEnum.values())
                .map(permissaoEnum -> new Permissao(null, permissaoEnum.name()))
                .collect(Collectors.toList());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
