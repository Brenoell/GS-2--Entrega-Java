
package com.fiap.minicity.handler;

import com.fiap.minicity.dto.ErroDTO;
import com.fiap.minicity.exception.CredenciaisInvalidasException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.ConflitoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    // Trata conflitos, como cadastros duplicados
    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroDTO> tratarConflito(
            ConflitoException e
    ) {
        return criarResposta(HttpStatus.CONFLICT, e.getMessage());
    }

    // Trata métodos HTTP não permitidos
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroDTO> tratarMetodoNaoPermitido(
            HttpRequestMethodNotSupportedException e
    ) {
        return criarResposta(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Método HTTP não permitido para este endpoint."
        );
    }

    // Trata erros de validação do Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> tratarValidacao(
            MethodArgumentNotValidException e
    ) {

        String mensagem = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return criarResposta(HttpStatus.BAD_REQUEST, mensagem);
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
