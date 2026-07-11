package br.tec.itlabs.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "curso_disciplinas")
public class CursoDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected CursoDisciplina() {}

    public CursoDisciplina(Curso curso, Disciplina disciplina) {
        this.curso = curso;
        this.disciplina = disciplina;
        this.criadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Curso getCurso() {
        return curso;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
