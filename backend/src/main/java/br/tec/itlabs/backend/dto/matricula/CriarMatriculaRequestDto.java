package br.tec.itlabs.backend.dto.matricula;

import jakarta.validation.constraints.NotNull;

public class CriarMatriculaRequestDto {

    @NotNull(message = "O aluno é obrigatório.")
    private Long alunoId;

    @NotNull(message = "A turma é obrigatória.")
    private Long turmaId;

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getTurmaId() {
        return turmaId;
    }
}