package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.Curso;
import br.tec.itlabs.backend.entity.CursoDisciplina;
import br.tec.itlabs.backend.entity.Disciplina;
import br.tec.itlabs.backend.exception.RecursoNaoEncontradoException;
import br.tec.itlabs.backend.exception.RegraDeNegocioException;
import br.tec.itlabs.backend.repository.CursoDisciplinaRepository;
import br.tec.itlabs.backend.repository.CursoRepository;
import br.tec.itlabs.backend.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoDisciplinaService {
    private final CursoDisciplinaRepository cursoDisciplinaRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public CursoDisciplinaService(
            CursoDisciplinaRepository cursoDisciplinaRepository,
            CursoRepository cursoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.cursoDisciplinaRepository = cursoDisciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @Transactional
    public CursoDisciplina associar(Long cursoId, Long disciplinaId) {
        Curso curso = buscarCursoPorId(cursoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (cursoDisciplinaRepository.existsByCursoIdAndDisciplinaId(cursoId, disciplinaId)) {
            throw new RegraDeNegocioException("Esta disciplina já está associada ao curso informado.");
        }

        CursoDisciplina cursoDisciplina = new CursoDisciplina(curso, disciplina);
        return cursoDisciplinaRepository.save(cursoDisciplina);
    }

    @Transactional(readOnly = true)
    public List<CursoDisciplina> listar() {
        return cursoDisciplinaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CursoDisciplina buscarPorId(Long id) {
        return cursoDisciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Associação entre curso e disciplina não encontrada."));
    }

    @Transactional
    public void remover(Long id) {
        CursoDisciplina cursoDisciplina = buscarPorId(id);
        cursoDisciplinaRepository.delete(cursoDisciplina);
    }

    private Curso buscarCursoPorId(Long cursoId) {
        return cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado."));
    }

    private Disciplina buscarDisciplinaPorId(Long disciplinaId) {
        return disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }
}
