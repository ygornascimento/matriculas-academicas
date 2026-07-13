package br.tec.itlabs.backend.exception;

import br.tec.itlabs.backend.dto.error.ErroCampoDto;
import br.tec.itlabs.backend.dto.error.ErroResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> tratarErroDeValidacao(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ErroCampoDto> campos = exception.getFieldErrors()
                .stream()
                .map(erro -> new ErroCampoDto(
                        erro.getField(),
                        erro.getDefaultMessage()
                ))
                .toList();

        ErroResponseDto response = new ErroResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Existem campos inválidos na requisição.",
                request.getRequestURI(),
                campos
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDto> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request
    ) {
        ErroResponseDto response = new ErroResponseDto(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponseDto> tratarRegraDeNegocio(
            RegraDeNegocioException exception,
            HttpServletRequest request
    ) {
        ErroResponseDto response = new ErroResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de negócio violada",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.badRequest().body(response);
    }
}
