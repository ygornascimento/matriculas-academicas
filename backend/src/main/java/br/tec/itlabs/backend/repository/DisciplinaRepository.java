package br.tec.itlabs.backend.repository;

import br.tec.itlabs.backend.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}
