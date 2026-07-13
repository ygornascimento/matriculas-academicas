package br.tec.itlabs.backend.dto.turma;

import br.tec.itlabs.backend.entity.Turma;

import java.time.Instant;

public record TurmaResponseDto(
        Long id,
        Long cursoDisciplinaId,
        String codigo,
        String periodo,
        Integer limiteVagas,
        Integer vagasOcupadas,
        String status,
        Instant criadoEm,
        Instant atualizadoEm
) {

    public static TurmaResponseDto fromEntity(Turma turma) {
        return new TurmaResponseDto(
                turma.getId(),
                turma.getCursoDisciplina().getId(),
                turma.getCodigo(),
                turma.getPeriodo(),
                turma.getLimiteVagas(),
                turma.getVagasOcupadas(),
                turma.getStatus().name(),
                turma.getCriadoEm(),
                turma.getAtualizadoEm()
        );
    }
}