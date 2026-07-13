package br.tec.itlabs.backend.dto.cursodisciplina;

import jakarta.validation.constraints.NotNull;

public class CriarCursoDisciplinaRequestDto {

    @NotNull(message = "O curso é obrigatório.")
    private Long cursoId;

    @NotNull(message = "A disciplina é obrigatória.")
    private Long disciplinaId;

    public Long getCursoId() {
        return cursoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }
}