package br.tec.itlabs.backend.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "matriculas")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMatricula status;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(name = "confirmada_em")
    private Instant confirmadaEm;

    @Column(name = "cancelada_em")
    private Instant canceladaEm;

    protected Matricula() {
        // Necessário para o JPA
    }

    public Matricula(Aluno aluno, Turma turma) {
        this.aluno = aluno;
        this.turma = turma;
        this.status = StatusMatricula.PENDENTE;
        this.criadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public StatusMatricula getStatus() {
        return status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getConfirmadaEm() {
        return confirmadaEm;
    }

    public Instant getCanceladaEm() {
        return canceladaEm;
    }

    public boolean estaPendente() {
        return StatusMatricula.PENDENTE.equals(this.status);
    }

    public boolean estaConfirmada() {
        return StatusMatricula.CONFIRMADA.equals(this.status);
    }

    public boolean estaCancelada() {
        return StatusMatricula.CANCELADA.equals(this.status);
    }

    public void confirmar() {
        if (!estaPendente()) {
            throw new IllegalStateException("Somente matrículas pendentes podem ser confirmadas.");
        }

        this.status = StatusMatricula.CONFIRMADA;
        this.confirmadaEm = Instant.now();
    }

    public void cancelar() {
        if (estaCancelada()) {
            throw new IllegalStateException("Matrícula já está cancelada.");
        }

        this.status = StatusMatricula.CANCELADA;
        this.canceladaEm = Instant.now();
    }

}
