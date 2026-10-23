package com.frigo.frigotec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CnpjJaExisteException extends RuntimeException {
    public CnpjJaExisteException(String cnpjJáCadastrado) {
     super(cnpjJáCadastrado);}
}
