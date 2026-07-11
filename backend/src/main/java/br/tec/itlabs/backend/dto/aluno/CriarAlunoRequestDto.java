package br.tec.itlabs.backend.dto.aluno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class CriarAlunoRequestDto {

    @NotBlank(message = "O nome do aluno é obrigatório.")
    private String nome;

    @NotBlank(message = "O e-mail do aluno é obrigatório.")
    @Email(message = "O email informado é inválido.")
    private String email;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
