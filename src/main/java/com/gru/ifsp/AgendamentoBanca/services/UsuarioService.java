package com.gru.ifsp.AgendamentoBanca.services;

import com.gru.ifsp.AgendamentoBanca.entity.Permissao;
import com.gru.ifsp.AgendamentoBanca.entity.Usuario;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.EmailAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.ProntuarioAlreadyExists;
import com.gru.ifsp.AgendamentoBanca.entity.exceptions.UserNotExistException;
import com.gru.ifsp.AgendamentoBanca.form.UserActivationForm;
import com.gru.ifsp.AgendamentoBanca.form.UsuarioForm;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.response.DadosParaAtivacaoResponse;
import com.gru.ifsp.AgendamentoBanca.response.UsuarioResponse;
import com.gru.ifsp.AgendamentoBanca.util.EmailSenderUtil;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UserRepository usuarioRepository;
    private PermissaoRepository permissaoRepository;

    private JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DadosParaAtivacaoResponse createUser(UsuarioForm usuarioForm) throws EmailAlreadyExists, ProntuarioAlreadyExists {

        validateUsuarioForm(usuarioForm);

        String formProntuarioFormatted = usuarioForm.getProntuario().toUpperCase(Locale.ROOT);
        String formEmail = usuarioForm.getEmail();
        String formNomePermissao = usuarioForm.getPermission().name();
        List<Permissao> permissoes = List.of(permissaoRepository.getByCodeName(formNomePermissao));
        String passwordEncoded = passwordEncoder.encode(usuarioForm.getPassword());
        String activationCode = EmailSenderUtil.generateCode();

        boolean shouldSendEmail = usuarioForm.isShouldSendConfirmationCode();
        boolean isUserActivated = !shouldSendEmail;

        Usuario usuario = new Usuario(null,
                formEmail,
                passwordEncoded,
                isUserActivated,
                permissoes,
                formProntuarioFormatted,
                usuarioForm.getUsername(),
                activationCode);

        var userInsertedOnDB = usuarioRepository.save(usuario);
        boolean isUserInserted = userInsertedOnDB.getId() != null;

        if (shouldSendEmail && isUserInserted) {
            sendConfirmationCode(userInsertedOnDB.getEmail(), activationCode);
        }
        var dadosParaAtivacao = new DadosParaAtivacaoResponse(userInsertedOnDB.getId(), userInsertedOnDB.getEmail(), userInsertedOnDB.getActivationCode());
        return dadosParaAtivacao;
    }


    private void validateUsuarioForm(UsuarioForm usuarioForm) {
        String formProntuarioFormatted = usuarioForm.getProntuario().toUpperCase(Locale.ROOT);
        String formEmail = usuarioForm.getEmail();

        if (usuarioRepository.existsByEmail(formEmail))
            throw new EmailAlreadyExists("Esse email já está cadastrado");
        if (usuarioRepository.existsByProntuario(formProntuarioFormatted))
            throw new ProntuarioAlreadyExists("Esse prontuario ja está cadastrado");
        if (usuarioForm.getPermission() == null)
            throw new RuntimeException("Esta permissão/cargo não existe");
    }


    private void sendConfirmationCode(String email, String codeLabel) {
        String confirmationLabel = "Código de confirmação:";
        EmailSenderUtil.sendEmail(javaMailSender, email, confirmationLabel + codeLabel);
    }


    public boolean disableUser(String email) {
        if (!usuarioRepository.existsByEmail(email)) throw new UserNotExistException("Este usuário não existe");

        Usuario usuario = usuarioRepository.findByEmail(email);
        usuario.setEnabled(false);

        return true;
    }

    public List<UsuarioResponse> listUsers() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::new).collect(Collectors.toList());
    }

    public boolean userExistsByID(Long id) {
        return usuarioRepository.existsById(id);
    }

    public boolean isUserActivated(Long id) {
        var usuario = usuarioRepository.getById(id);
        return usuario.isEnabled();
    }

    public void activateUser(UserActivationForm form) {
        if (!userExistsByID(form.id)) throw new RuntimeException("Não tem usuário com esse ID");
        if (isUserActivated(form.id)) throw new RuntimeException("Esse usuário já está ativado");

        var usuario = usuarioRepository.getById(form.id);

        if (!form.activationCode.equalsIgnoreCase(usuario.getActivationCode()))
            throw new RuntimeException("O código passado está invalido!");

        usuario.setEnabled(true);
        usuario.setActivationCode("");
        usuarioRepository.save(usuario);
    }


    public void resendActivationCode(String email) {
        if (!usuarioRepository.existsByEmail(email))
            throw new RuntimeException("Não existe usuário cadastrado com esse email");

        String newActivationCode = EmailSenderUtil.generateCode();
        var usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEnabled())
            throw new RuntimeException("Este usuário já está ativo!");

        usuario.setActivationCode(newActivationCode);
        EmailSenderUtil.sendEmail(javaMailSender, email, newActivationCode);
    }

}
