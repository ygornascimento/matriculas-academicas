package br.tec.itlabs.backend.dto.curso;

import br.tec.itlabs.backend.entity.Curso;

import java.time.Instant;

public record CursoResponseDto(
        Long id,
        String nome,
        String descricao,
        Instant criadoEm,
        Instant atualizadoEm
) {

    public static CursoResponseDto fromEntity(Curso curso) {
        return new CursoResponseDto(
                curso.getId(),
                curso.getNome(),
                curso.getDescricao(),
                curso.getCriadoEm(),
                curso.getAtualizadoEm()
        );
    }
}