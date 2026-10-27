
package com.frigo.frigotec.email;

import com.frigo.frigotec.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrcamentoEmailListener {

    private final EmailService emailService;

    public OrcamentoEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    // Só dispara depois do commit: se o orçamento sofrer rollback, nenhum e-mail sai.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoSalvarOrcamento(OrcamentoSalvoEvent evento) {
        emailService.enviarOrcamento(evento.mensagemId(), evento.valor());
    }
}