package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.CursoDisciplina;
import br.tec.itlabs.backend.entity.Turma;
import br.tec.itlabs.backend.exception.RecursoNaoEncontradoException;
import br.tec.itlabs.backend.exception.RegraDeNegocioException;
import br.tec.itlabs.backend.repository.CursoDisciplinaRepository;
import br.tec.itlabs.backend.repository.TurmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurmaService {
    private final TurmaRepository turmaRepository;
    private final CursoDisciplinaRepository cursoDisciplinaRepository;

    public TurmaService(
            TurmaRepository turmaRepository,
            CursoDisciplinaRepository cursoDisciplinaRepository
    ) {
        this.turmaRepository = turmaRepository;
        this.cursoDisciplinaRepository = cursoDisciplinaRepository;
    }

    @Transactional
    public Turma criar(Long cursoDisciplinaId, String codigo, String periodo, Integer limiteVagas) {
        CursoDisciplina cursoDisciplina = buscarCursoDisciplinaPorId(cursoDisciplinaId);

        Turma turma = new Turma(cursoDisciplina, codigo, periodo, limiteVagas);
        return turmaRepository.save(turma);
    }

    @Transactional(readOnly = true)
    public List<Turma> listar() {
        return turmaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Turma buscarPorId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada."));
    }

    @Transactional
    public Turma atualizar(Long id, Long cursoDisciplinaId, String codigo, String periodo, Integer limiteVagas) {
        Turma turma = buscarPorId(id);
        CursoDisciplina cursoDisciplina = buscarCursoDisciplinaPorId(cursoDisciplinaId);

        if (limiteVagas < turma.getVagasOcupadas()) {
            throw new RegraDeNegocioException("O limite de vagas não pode ser menor que as vagas já ocupadas.");
        }

        turma.alterarDados(cursoDisciplina, codigo, periodo, limiteVagas);
        return turmaRepository.save(turma);
    }

    @Transactional
    public Turma abrir(Long id) {
        Turma turma = buscarPorId(id);
        turma.abrir();
        return turmaRepository.save(turma);
    }

    @Transactional
    public Turma fechar(Long id) {
        Turma turma = buscarPorId(id);
        turma.fechar();
        return turmaRepository.save(turma);
    }

    @Transactional
    public void excluir(Long id) {
        Turma turma = buscarPorId(id);
        turmaRepository.delete(turma);
    }

    private CursoDisciplina buscarCursoDisciplinaPorId(Long cursoDisciplinaId) {
        return cursoDisciplinaRepository.findById(cursoDisciplinaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Associação entre curso e disciplina não encontrada."));
    }
}
