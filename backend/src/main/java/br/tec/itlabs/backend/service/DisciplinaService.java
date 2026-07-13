package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.Disciplina;
import br.tec.itlabs.backend.exception.RecursoNaoEncontradoException;
import br.tec.itlabs.backend.exception.RegraDeNegocioException;
import br.tec.itlabs.backend.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @Transactional
    public Disciplina criar(String nome, Integer cargaHoraria) {
        if (disciplinaRepository.existsByNome(nome)) {
            throw new RegraDeNegocioException("Já existe uma disciplina cadastrada com este nome.");
        }
        Disciplina disciplina = new Disciplina(nome, cargaHoraria);
        return disciplinaRepository.save(disciplina);
    }

    @Transactional(readOnly = true)
    public List<Disciplina> listar() {
        return disciplinaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Disciplina buscarPorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }

    @Transactional
    public Disciplina atualizar(Long id, String nome, Integer cargaHoraria) {
        Disciplina disciplina = buscarPorId(id);
        if (disciplinaRepository.existsByNomeAndIdNot(nome, id)) {
            throw new RegraDeNegocioException("Já existe outra disciplina cadastrada com este nome.");
        }
        disciplina.alterarDados(nome, cargaHoraria);
        return disciplinaRepository.save(disciplina);
    }

    @Transactional
    public void excluir(Long id) {
        Disciplina disciplina = buscarPorId(id);
        disciplinaRepository.delete(disciplina);
    }
}
