package com.gru.ifsp.AgendamentoBanca;

import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AgendamentoBancaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamentoBancaApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository) {
		return args -> {
			userRepository.save(new Usuario(null, "admin@admin.com", passwordEncoder().encode("admin")));
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
