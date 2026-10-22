package com.frigo.frigotec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BuscaEncontradaException extends RuntimeException {
    public BuscaEncontradaException(String message) {
        super(message);
    }
}
