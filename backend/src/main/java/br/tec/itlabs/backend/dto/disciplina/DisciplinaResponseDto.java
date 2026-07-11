package br.tec.itlabs.backend.dto.disciplina;

import br.tec.itlabs.backend.entity.Disciplina;

import java.time.Instant;

public record DisciplinaResponseDto(
        Long id,
        String nome,
        Integer cargaHoraria,
        Instant criadoEm,
        Instant atualizadoEm
) {

    public static DisciplinaResponseDto fromEntity(Disciplina disciplina) {
        return new DisciplinaResponseDto(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getCargaHoraria(),
                disciplina.getCriadoEm(),
                disciplina.getAtualizadoEm()
        );
    }
}