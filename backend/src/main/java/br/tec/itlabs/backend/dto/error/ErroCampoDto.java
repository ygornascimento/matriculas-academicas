package br.tec.itlabs.backend.dto.error;

public record ErroCampoDto(
        String campo,
        String mensagem
) {
}
