package br.tec.itlabs.backend.repository;

import br.tec.itlabs.backend.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
