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
@Table(name = "turmas")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_disciplina_id", nullable = false)
    private CursoDisciplina cursoDisciplina;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 30)
    private String periodo;

    @Column(name = "limite_vagas", nullable = false)
    private Integer limiteVagas;

    @Column(name = "vagas_ocupadas", nullable = false)
    private Integer vagasOcupadas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTurma status;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    protected Turma() {}

    public Turma(CursoDisciplina cursoDisciplina, String codigo, String periodo, Integer limiteVagas) {
        this.cursoDisciplina = cursoDisciplina;
        this.codigo = codigo;
        this.periodo = periodo;
        this.limiteVagas = limiteVagas;
        this.vagasOcupadas = 0;
        this.status = StatusTurma.ABERTA;
        this.criadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public CursoDisciplina getCursoDisciplina() {
        return cursoDisciplina;
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

    public Integer getVagasOcupadas() {
        return vagasOcupadas;
    }

    public StatusTurma getStatus() {
        return status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public boolean estaAberta() {
        return StatusTurma.ABERTA.equals(this.status);
    }

    public boolean possuiVagaDisponivel() {
        return this.vagasOcupadas < this.limiteVagas;
    }

    public void consumirVaga() {
        if (!possuiVagaDisponivel()) {
            throw new IllegalStateException("Turma não possui vagas disponíveis.");
        }

        this.vagasOcupadas++;
        this.atualizadoEm = Instant.now();
    }

    public void liberarVaga() {
        if (this.vagasOcupadas <= 0) {
            throw new IllegalStateException("Não há vagas ocupadas para liberar.");
        }

        this.vagasOcupadas--;
        this.atualizadoEm = Instant.now();
    }

    public void fechar() {
        this.status = StatusTurma.FECHADA;
        this.atualizadoEm = Instant.now();
    }

    public void abrir() {
        this.status = StatusTurma.ABERTA;
        this.atualizadoEm = Instant.now();
    }

    public void alterarDados(
            CursoDisciplina cursoDisciplina,
            String codigo,
            String periodo,
            Integer limiteVagas
    ) {
        this.cursoDisciplina = cursoDisciplina;
        this.codigo = codigo;
        this.periodo = periodo;
        this.limiteVagas = limiteVagas;
        this.atualizadoEm = Instant.now();
    }
}
