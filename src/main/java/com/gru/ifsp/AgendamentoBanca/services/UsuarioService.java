package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.Permissao;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.ProntuarioAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.UserNotExistException;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.response.UsuarioResponse;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UserRepository usuarioRepository;
    private PermissaoRepository permissaoRepository;

    private JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario createUser(UsuarioForm usuarioForm) throws EmailAlreadyExists, ProntuarioAlreadyExists {
        if (usuarioRepository.existsByEmail(usuarioForm.getEmail()))
            throw new EmailAlreadyExists("Esse email já está cadastrado");
        if (usuarioRepository.existsByProntuario(usuarioForm.getProntuario()))
            throw new ProntuarioAlreadyExists("Esse prontuario ja está cadastrado");

        List<Permissao> permissaoList = List.of(permissaoRepository.getByCodeName(usuarioForm.getPermission().name()));
        String passwordEncoded = passwordEncoder.encode(usuarioForm.getPassword());
        String code = "";
        boolean shouldSendEmail = usuarioForm.isShouldSendConfirmationCode();
        boolean isEnabled = true;

        if (shouldSendEmail) {
            isEnabled = false;
            code = generateCode();
        }

        Usuario usuario = new Usuario(null,
                usuarioForm.getEmail(),
                passwordEncoded,
                isEnabled,
                permissaoList,
                usuarioForm.getProntuario().toUpperCase(Locale.ROOT),
                usuarioForm.getUsername(),
                code);

        usuarioRepository.save(usuario);

        if (shouldSendEmail) {
            String confirmationCodeLabel = "Código de confirmação:" + code;
            sendEmail(usuarioForm.getEmail(), confirmationCodeLabel);
        }


        return usuario;
    }

    private String generateCode() {
        int leftLimit = 97; //letter 'a'
        int rightLimit = 122; //letter 'b'
        int stringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    private boolean sendEmail(String email, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(confirmationCode);
        message.setTo(email);
        message.setFrom("samuel.capusesera@aluno.ifsp.edu.br");

        try {
            javaMailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disableUser(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            //TODO: fazer com que o usuário só se autentique quando estiver com isEnabled = true;
            Usuario usuario = usuarioRepository.findByEmail(email);
            usuarioRepository.delete(usuario);
        } else {
            throw new UserNotExistException("Este usuário não existe");
        }
        return true;
    }

    public List<UsuarioResponse> listUsers() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::new).collect(Collectors.toList());
    }


}
