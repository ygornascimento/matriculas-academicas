package br.tec.itlabs.backend.dto.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CriarCursoRequestDto {

    @NotBlank(message = "O nome do curso é obrigatório.")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String descricao;

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}