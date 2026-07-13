package br.tec.itlabs.backend.dto.aluno;

import br.tec.itlabs.backend.entity.Aluno;

import java.time.Instant;

public record AlunoResponseDto(
        Long id,
        String nome,
        String email,
        Instant criadoEm,
        Instant atualizadoEm
) {

    public static AlunoResponseDto fromEntity(Aluno aluno) {
        return new AlunoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getCriadoEm(),
                aluno.getAtualizadoEm()
        );
    }
}
