package com.gru.ifsp.AgendamentoBanca.services.impl;

import com.gru.ifsp.AgendamentoBanca.model.AgendamentoBanca;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import com.gru.ifsp.AgendamentoBanca.services.contracts.EmailService;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
    public boolean sendDifferencesBetweenNewAndOldBancaUpdate(AgendamentoBanca oldBanca, AgendamentoBanca newBanca) {
        var changedProperties = new ArrayList<String>();

        getDifferencesBetweenLists(oldBanca.getParticipantes(), newBanca.getParticipantes(), changedProperties, "participante");
        getDifferencesBetweenLists(oldBanca.getAvaliadores(), newBanca.getAvaliadores(), changedProperties, "participante");

        oldBanca.setParticipantes(null);
        oldBanca.setAvaliadores(null);
        newBanca.setParticipantes(null);
        newBanca.setAvaliadores(null);

        getDifferenceBetweenFields(oldBanca, newBanca, changedProperties);

        //TODO send changes in email
        changedProperties.forEach(System.out::println);
        return true;

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
