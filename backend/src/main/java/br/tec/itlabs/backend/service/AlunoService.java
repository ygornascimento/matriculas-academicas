package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.Aluno;
import br.tec.itlabs.backend.exception.RecursoNaoEncontradoException;
import br.tec.itlabs.backend.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {

        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno criar(String nome, String email) {
        Aluno aluno = new Aluno(nome, email);
        return alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));
    }

    @Transactional
    public Aluno atualizar(Long id, String nome, String email) {
        Aluno aluno = buscarPorId(id);
        aluno.alterarDados(nome, email);
        return alunoRepository.save(aluno);
    }

    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscarPorId(id);
        alunoRepository.delete(aluno);
    }
}
