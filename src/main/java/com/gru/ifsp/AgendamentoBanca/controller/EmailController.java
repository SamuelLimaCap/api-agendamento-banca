package com.gru.ifsp.AgendamentoBanca.controller;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public String sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("teste");
        message.setTo("samueldelimapessoal@gmail.com");
        message.setFrom("samuel.capusesera@aluno.ifsp.edu.br");

        try {
            mailSender.send(message);
            return "Email enviado com sucesso";
        } catch (MailException e) {
            return "Erro ao enviar e-mail";
        }
    }
}
