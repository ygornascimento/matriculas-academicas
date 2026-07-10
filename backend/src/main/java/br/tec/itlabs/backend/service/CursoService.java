package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.Curso;
import br.tec.itlabs.backend.exception.RecursoNaoEncontradoException;
import br.tec.itlabs.backend.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public Curso criar(String nome, String descricao) {
        Curso curso = new Curso(nome, descricao);
        return cursoRepository.save(curso);
    }

    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado."));
    }

    @Transactional
    public Curso atualizar(Long id, String nome, String descricao) {
        Curso curso = buscarPorId(id);
        curso.alterarDados(nome, descricao);
        return cursoRepository.save(curso);
    }

    @Transactional
    public void excluir(Long id) {
        Curso curso = buscarPorId(id);
        cursoRepository.delete(curso);
    }

}
