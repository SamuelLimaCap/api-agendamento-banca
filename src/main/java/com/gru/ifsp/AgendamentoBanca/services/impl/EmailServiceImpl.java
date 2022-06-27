package com.gru.ifsp.AgendamentoBanca.services.impl;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.EmailTemplate;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.services.contracts.EmailService;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;


    @Override
    public boolean sendConfirmationCodeEmail(String email) {
        return false;
    }

    @Override
    public boolean sendEmailToConfirmChangesOnBanca(String email, Long bancaId, Long userId) {
        try {
            EmailTemplate template = new EmailTemplate(
                    "Confirme sua inscrição para a banca",
                    "Clique no botão abaixo para confirmar sua inscrição",
                    "Status atual: AGUARDANDO",
                    "",
                    ""
            );
            MimeMessage smm = javaMailSender.createMimeMessage();
            smm.setSubject("Você foi adicionado em uma banca");
            MimeMessageHelper mmh = new MimeMessageHelper(smm, true);

            mmh.setFrom("calendariodebanca@gmail.com");
            mmh.setTo(email);
            mmh.setText(template.toString());
            javaMailSender.send(smm);
            return true;
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendDifferencesBetweenNewAndOldBancaUpdate(AgendamentoBanca oldBanca, AgendamentoBanca newBanca, String email) {
        var changedProperties = new ArrayList<String>();

        getDifferencesBetweenLists(oldBanca.getParticipantes(), newBanca.getParticipantes(), changedProperties, "participante");
        getDifferencesBetweenLists(oldBanca.getAvaliadores(), newBanca.getAvaliadores(), changedProperties, "participante");

        oldBanca.setParticipantes(null);
        oldBanca.setAvaliadores(null);
        newBanca.setParticipantes(null);
        newBanca.setAvaliadores(null);

        getDifferenceBetweenFields(oldBanca, newBanca, changedProperties);


        try {
            EmailTemplate template = new EmailTemplate(
                    "Mudanças no banca " +oldBanca.getTitulo(),
                    "Aqui estão as mudanças feitas na banca",
                    changedProperties.stream().reduce("\n", (partialString, element) -> partialString + element),
                    "",
                    ""
            );
            MimeMessage smm = javaMailSender.createMimeMessage();
            smm.setSubject("Você foi adicionado em uma banca");
            MimeMessageHelper mmh = new MimeMessageHelper(smm, true);

            mmh.setFrom("calendariodebanca@gmail.com");
            mmh.setTo(email);
            mmh.setText(template.toString());
            javaMailSender.send(smm);
            return true;
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendCustomMessageEmail(String title, String description, String content, String email) {
        try {
            EmailTemplate template = new EmailTemplate(
                    title,
                    description,
                    content,
                    "",
                    ""
            );
            MimeMessage smm = javaMailSender.createMimeMessage();
            smm.setSubject("Você foi adicionado em uma banca");
            MimeMessageHelper mmh = new MimeMessageHelper(smm, true);

            mmh.setFrom("calendariodebanca@gmail.com");
            mmh.setTo(email);
            mmh.setText(template.toString());
            javaMailSender.send(smm);
            return true;
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getDifferenceBetweenFields(AgendamentoBanca oldBanca, AgendamentoBanca newBanca, List<String> changedProperties) {
        var differences = ObjectDifferBuilder.startBuilding().
                comparison().ofType(LocalDateTime.class).toUseEqualsMethod().and().build()
                .compare(oldBanca, newBanca);
        if (differences.hasChanges()) {
            differences.visit(new DiffNode.Visitor() {
                @Override
                public void node(DiffNode diffNode, Visit visit) {
                    if (!diffNode.hasChildren()) {
                        var oldValue = diffNode.canonicalGet(oldBanca);
                        var newValue = diffNode.canonicalGet(newBanca);
                        changedProperties.add(diffNode.getPropertyName() + ": Alterado de \"" + oldValue + "\" para \"" + newValue);
                    }
                }
            });
        }
    }

    private void getDifferencesBetweenLists(List<Usuario> oldList, List<Usuario> newList, List<String> changedProperties, String typeOfList) {
        if (!oldList.equals(newList)) {
            newList.forEach(user -> {
                if (!oldList.contains(user))
                    changedProperties.add(typeOfList + ": " + user.getUsername());
            });
        }

    }


}
