package br.tec.itlabs.backend.dto.matricula;

import br.tec.itlabs.backend.entity.Matricula;

import java.time.Instant;

public record MatriculaResponseDto(
        Long id,
        Long alunoId,
        Long turmaId,
        String status,
        Instant criadoEm,
        Instant confirmadaEm,
        Instant canceladaEm
) {

    public static MatriculaResponseDto fromEntity(Matricula matricula) {
        return new MatriculaResponseDto(
                matricula.getId(),
                matricula.getAluno().getId(),
                matricula.getTurma().getId(),
                matricula.getStatus().name(),
                matricula.getCriadoEm(),
                matricula.getConfirmadaEm(),
                matricula.getCanceladaEm()
        );
    }
}