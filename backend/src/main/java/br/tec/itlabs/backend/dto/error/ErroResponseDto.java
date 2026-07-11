package br.tec.itlabs.backend.dto.error;

import java.time.Instant;
import java.util.List;

public record ErroResponseDto(
        Instant timestamp,
        Integer status,
        String erro,
        String mensagem,
        String path,
        List<ErroCampoDto> campos
) {
}
