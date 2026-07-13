package br.tec.itlabs.backend.repository;

import br.tec.itlabs.backend.entity.CursoDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoDisciplinaRepository extends JpaRepository<CursoDisciplina, Long> {
    boolean existsByCursoIdAndDisciplinaId(Long cursoId, Long disciplinaId);
}
