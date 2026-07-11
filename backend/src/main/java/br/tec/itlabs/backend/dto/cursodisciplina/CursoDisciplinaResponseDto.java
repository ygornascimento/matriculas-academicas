package br.tec.itlabs.backend.dto.cursodisciplina;

import br.tec.itlabs.backend.entity.CursoDisciplina;

import java.time.Instant;

public record CursoDisciplinaResponseDto(
        Long id,
        Long cursoId,
        Long disciplinaId,
        Instant criadoEm
) {

    public static CursoDisciplinaResponseDto fromEntity(CursoDisciplina cursoDisciplina) {
        return new CursoDisciplinaResponseDto(
                cursoDisciplina.getId(),
                cursoDisciplina.getCurso().getId(),
                cursoDisciplina.getDisciplina().getId(),
                cursoDisciplina.getCriadoEm()
        );
    }
}