package com.gru.ifsp.AgendamentoBanca.util;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Random;

public class EmailSenderUtil {

    public static String generateCode() {
        int leftLimit = 97; //letter 'a'
        int rightLimit = 122; //letter 'z'
        int stringLength = 6;
        Random random = new Random();
        String generatedString;

        generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public static void sendEmail(JavaMailSender javaMailSender, String email, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(confirmationCode);
        message.setTo(email);
        message.setFrom("samuel.capusesera@aluno.ifsp.edu.br");

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }

    }
}
