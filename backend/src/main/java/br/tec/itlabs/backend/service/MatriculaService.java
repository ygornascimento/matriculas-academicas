package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.Aluno;
import br.tec.itlabs.backend.entity.Matricula;
import br.tec.itlabs.backend.entity.Turma;
import br.tec.itlabs.backend.exception.RecursoNaoEncontradoException;
import br.tec.itlabs.backend.exception.RegraDeNegocioException;
import br.tec.itlabs.backend.repository.AlunoRepository;
import br.tec.itlabs.backend.repository.MatriculaRepository;
import br.tec.itlabs.backend.repository.TurmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatriculaService {
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            TurmaRepository turmaRepository
    ) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public Matricula criar(Long alunoId, Long turmaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Turma turma = buscarTurmaPorId(turmaId);

        if (!turma.estaAberta()) {
            throw new RegraDeNegocioException("Só é possível matricular aluno em turma aberta.");
        }

        if (matriculaRepository.existsByAlunoIdAndTurmaId(alunoId, turmaId)) {
            throw new RegraDeNegocioException("Aluno já possui matrícula nesta turma.");
        }

        Matricula matricula = new Matricula(aluno, turma);
        return matriculaRepository.save(matricula);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listar() {
        return matriculaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Matricula buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorAluno(Long alunoId) {
        buscarAlunoPorId(alunoId);
        return matriculaRepository.findByAlunoId(alunoId);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorTurma(Long turmaId) {
        buscarTurmaPorId(turmaId);
        return matriculaRepository.findByTurmaId(turmaId);
    }

    @Transactional
    public Matricula confirmar(Long id) {
        Matricula matricula = buscarPorId(id);
        Turma turma = matricula.getTurma();

        if (!matricula.estaPendente()) {
            throw new RegraDeNegocioException("Somente matrículas pendentes podem ser confirmadas.");
        }

        if (!turma.estaAberta()) {
            throw new RegraDeNegocioException("Só é possível confirmar matrícula de turma aberta.");
        }

        if (!turma.possuiVagaDisponivel()) {
            throw new RegraDeNegocioException("Turma não possui vagas disponíveis.");
        }

        turma.consumirVaga();
        matricula.confirmar();

        turmaRepository.save(turma);
        return matriculaRepository.save(matricula);
    }

    @Transactional
    public Matricula cancelar(Long id) {
        Matricula matricula = buscarPorId(id);
        Turma turma = matricula.getTurma();

        if (matricula.estaCancelada()) {
            throw new RegraDeNegocioException("Matrícula já está cancelada.");
        }

        if (matricula.estaConfirmada()) {
            turma.liberarVaga();
            turmaRepository.save(turma);
        }

        matricula.cancelar();
        return matriculaRepository.save(matricula);
    }

    private Aluno buscarAlunoPorId(Long alunoId) {
        return alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));
    }

    private Turma buscarTurmaPorId(Long turmaId) {
        return turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada."));
    }
}
