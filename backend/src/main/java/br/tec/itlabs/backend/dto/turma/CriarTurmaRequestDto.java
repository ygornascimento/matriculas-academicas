package br.tec.itlabs.backend.dto.turma;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CriarTurmaRequestDto {

    @NotNull(message = "A associação entre curso e disciplina é obrigatória.")
    private Long cursoDisciplinaId;

    @NotBlank(message = "O código da turma é obrigatório.")
    private String codigo;

    @NotBlank(message = "O período da turma é obrigatório.")
    private String periodo;

    @NotNull(message = "O limite de vagas é obrigatório.")
    @Positive(message = "O limite de vagas deve ser maior que zero.")
    private Integer limiteVagas;

    public Long getCursoDisciplinaId() {
        return cursoDisciplinaId;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public Integer getLimiteVagas() {
        return limiteVagas;
    }
}