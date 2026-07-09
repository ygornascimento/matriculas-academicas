package br.tec.itlabs.backend.repository;

import br.tec.itlabs.backend.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
}
