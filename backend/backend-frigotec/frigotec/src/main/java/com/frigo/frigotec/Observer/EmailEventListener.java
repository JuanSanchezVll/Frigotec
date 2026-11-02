package com.frigo.frigotec.Observer;

import com.frigo.frigotec.dto.StatusChamadoAlteradoEvento;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
@Component
public class EmailEventListener {

    @TransactionalEventListener
    public void aoAlterarStatus(StatusChamadoAlteradoEvento event) {
        // montar e enviar o e-mail para o usuário do chamado
    }
}
