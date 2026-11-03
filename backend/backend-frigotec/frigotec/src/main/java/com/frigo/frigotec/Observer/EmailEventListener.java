package com.frigo.frigotec.Observer;

import com.frigo.frigotec.dto.StatusChamadoAlteradoEvento;
import com.frigo.frigotec.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Observer (padrão de projeto do projeto): o ChamadoService só publica StatusChamadoAlteradoEvento; este ouvinte reage
 * e avisa o cliente por e-mail. Só dispara depois do commit: se o status não for gravado, nenhum e-mail sai.
 * Outros ouvintes (WhatsApp, por exemplo) podem ser adicionados sem alterar o serviço.
 */
@Component
public class EmailEventListener {

    private final EmailService emailService;

    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoAlterarStatus(StatusChamadoAlteradoEvento event) {
        emailService.enviarMudancaStatus(event.chamado().getId(), event.novoStatus());
    }
}
