package com.frigo.frigotec.exception;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Padrão único de erro da API (SDD seção 9): JSON com status, mensagem e, quando houver, os campos inválidos.
 * O front lê "mensagem" e mostra para o usuário.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record CampoInvalido(String campo, String mensagem) {}

    public record ErroResposta(int status, String mensagem, List<CampoInvalido> campos) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException e) {
        List<CampoInvalido> campos = e.getBindingResult().getFieldErrors().stream()
                .map(f -> new CampoInvalido(f.getField(), f.getDefaultMessage()))
                .toList();
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos. Confira os campos informados.", campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> corpoIlegivel(HttpMessageNotReadableException e) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido.", List.of());
    }

    /** Exceções de negócio já anotadas com @ResponseStatus; as demais seguem para o tratamento padrão (500). */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResposta> negocio(RuntimeException e) {
        ResponseStatus rs = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        if (rs == null) {
            throw e;
        }
        return resposta(rs.code(), e.getMessage(), List.of());
    }

    private ResponseEntity<ErroResposta> resposta(HttpStatus status, String mensagem, List<CampoInvalido> campos) {
        return ResponseEntity.status(status).body(new ErroResposta(status.value(), mensagem, campos));
    }
}
