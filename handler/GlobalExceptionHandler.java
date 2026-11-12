
package com.fiap.minicity.handler;

import com.fiap.minicity.dto.ErroDTO;
import com.fiap.minicity.exception.CredenciaisInvalidasException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> tratarNaoEncontrado(
            RecursoNaoEncontradoException e
    ) {
        return criarResposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroDTO> tratarRegraNegocio(
            RegraNegocioException e
    ) {
        return criarResposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroDTO> tratarCredenciaisInvalidas(
            CredenciaisInvalidasException e
    ) {
        return criarResposta(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    private ResponseEntity<ErroDTO> criarResposta(
            HttpStatus status,
            String mensagem
    ) {
        ErroDTO erro = new ErroDTO(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(erro);
    }
}
