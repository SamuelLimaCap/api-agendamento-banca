package com.gru.ifsp.AgendamentoBanca;

import com.gru.ifsp.AgendamentoBanca.model.Permissao;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.model.enums.PermissaoEnum;
import com.gru.ifsp.AgendamentoBanca.model.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.response.DadosParaAtivacaoResponse;
import com.gru.ifsp.AgendamentoBanca.services.UsuarioService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PermissaoRepository permissaoRepository;

    @Mock
    private JavaMailSender javaMailSender;

    private UsuarioService usuarioService;

    private UsuarioForm form;

    private String password;

    @BeforeEach
    void initUseCase() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(userRepository, permissaoRepository, javaMailSender);
        form = new UsuarioForm(
                "teste@teste.com",
                "teste123teste",
                "gu3201565",
                "usuarioTeste",
                PermissaoEnum.ADMIN,
                false
        );
        password = new BCryptPasswordEncoder().encode(form.getPassword());
    }


    @Test
    public void shouldCreateUser() {
        Usuario returnedUser = new Usuario(0L,
                form.getEmail(),
                new BCryptPasswordEncoder().encode(form.getPassword()),
                false,
                List.of(),
                form.getProntuario().toUpperCase(Locale.ROOT),
                form.getUsername()
        );

        Mockito.when(userRepository.save(Mockito.any(Usuario.class))).thenReturn(returnedUser);
        Mockito.when(permissaoRepository.getByCodeName(Mockito.any(String.class))).thenReturn(new Permissao(0L, PermissaoEnum.ADMIN.name()));
        DadosParaAtivacaoResponse usuario = usuarioService.createUser(form);

        Assertions.assertThat(usuario).isNotNull();
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyExistsWithThatEmail() {
        Usuario insertedUser = new Usuario(null,
                form.getEmail(),
                password,
                false,
                List.of(),
                form.getProntuario().toUpperCase(Locale.ROOT),
                form.getUsername()
        );

        Usuario returnedUser = new Usuario(0L,
                form.getEmail(),
                password,
                false,
                List.of(),
                form.getProntuario().toUpperCase(Locale.ROOT),
                form.getUsername()
        );

        lenient().when(userRepository.save(insertedUser)).thenReturn(returnedUser);
        lenient().when(permissaoRepository.getByCodeName(any(String.class))).thenReturn(new Permissao(0L, PermissaoEnum.ADMIN.name()));


        lenient().when(userRepository.existsByEmail(form.getEmail())).thenReturn(false);
        DadosParaAtivacaoResponse firstTime = usuarioService.createUser(form);

        lenient().when(userRepository.existsByEmail(form.getEmail())).thenReturn(true);
        EmailAlreadyExists e = Assert.assertThrows(EmailAlreadyExists.class, () -> {usuarioService.createUser(form);});

        org.junit.jupiter.api.Assertions.assertNotNull(e);
    }
}
