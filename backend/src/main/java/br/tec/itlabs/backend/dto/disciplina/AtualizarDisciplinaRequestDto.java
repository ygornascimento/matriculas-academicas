package br.tec.itlabs.backend.dto.disciplina;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AtualizarDisciplinaRequestDto {

    @NotBlank(message = "O nome da disciplina é obrigatório.")
    private String nome;

    @NotNull(message = "A carga horária da disciplina é obrigatória.")
    @Positive(message = "A carga horária deve ser maior que zero.")
    private Integer cargaHoraria;

    public String getNome() {
        return nome;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }
}