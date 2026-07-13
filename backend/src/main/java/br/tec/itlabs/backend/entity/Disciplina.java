package br.tec.itlabs.backend.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    protected Disciplina() {}

    public Disciplina(String nome, Integer cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.criadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public void alterarDados(String nome, Integer cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.atualizadoEm = Instant.now();
    }
}
