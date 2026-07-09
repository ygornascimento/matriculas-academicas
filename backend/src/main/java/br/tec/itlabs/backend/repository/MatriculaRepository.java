package br.tec.itlabs.backend.repository;

import br.tec.itlabs.backend.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);
    List<Matricula> findByAlunoId(Long alunoId);
    List<Matricula> findByTurmaId(Long turmaId);
}
