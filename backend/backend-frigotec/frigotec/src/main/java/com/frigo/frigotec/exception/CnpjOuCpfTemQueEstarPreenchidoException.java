package com.frigo.frigotec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CnpjOuCpfTemQueEstarPreenchidoException extends RuntimeException {
    public CnpjOuCpfTemQueEstarPreenchidoException(String message) {
        super(message);
    }
}
