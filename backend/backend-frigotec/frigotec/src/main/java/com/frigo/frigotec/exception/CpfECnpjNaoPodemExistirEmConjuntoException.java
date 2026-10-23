package com.frigo.frigotec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CpfECnpjNaoPodemExistirEmConjuntoException extends RuntimeException {
    public CpfECnpjNaoPodemExistirEmConjuntoException(String message) {
        super(message);
    }
}
