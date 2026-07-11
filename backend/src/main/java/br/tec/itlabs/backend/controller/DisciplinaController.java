package br.tec.itlabs.backend.controller;

import br.tec.itlabs.backend.dto.disciplina.AtualizarDisciplinaRequestDto;
import br.tec.itlabs.backend.dto.disciplina.CriarDisciplinaRequestDto;
import br.tec.itlabs.backend.dto.disciplina.DisciplinaResponseDto;
import br.tec.itlabs.backend.entity.Disciplina;
import br.tec.itlabs.backend.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponseDto> criar(@RequestBody @Valid CriarDisciplinaRequestDto request) {
        Disciplina disciplina = disciplinaService.criar(request.getNome(), request.getCargaHoraria());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DisciplinaResponseDto.fromEntity(disciplina));
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDto>> listar() {
        List<DisciplinaResponseDto> disciplinas = disciplinaService.listar()
                .stream()
                .map(DisciplinaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDto> buscarPorId(@PathVariable Long id) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);

        return ResponseEntity.ok(DisciplinaResponseDto.fromEntity(disciplina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarDisciplinaRequestDto request
    ) {
        Disciplina disciplina = disciplinaService.atualizar(
                id,
                request.getNome(),
                request.getCargaHoraria()
        );

        return ResponseEntity.ok(DisciplinaResponseDto.fromEntity(disciplina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        disciplinaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}