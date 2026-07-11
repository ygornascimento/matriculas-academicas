package br.tec.itlabs.backend.controller;

import br.tec.itlabs.backend.dto.curso.AtualizarCursoRequestDto;
import br.tec.itlabs.backend.dto.curso.CriarCursoRequestDto;
import br.tec.itlabs.backend.dto.curso.CursoResponseDto;
import br.tec.itlabs.backend.entity.Curso;
import br.tec.itlabs.backend.service.CursoService;
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
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoResponseDto> criar(@RequestBody @Valid CriarCursoRequestDto request) {
        Curso curso = cursoService.criar(request.getNome(), request.getDescricao());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CursoResponseDto.fromEntity(curso));
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> listar() {
        List<CursoResponseDto> cursos = cursoService.listar()
                .stream()
                .map(CursoResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> buscarPorId(@PathVariable Long id) {
        Curso curso = cursoService.buscarPorId(id);

        return ResponseEntity.ok(CursoResponseDto.fromEntity(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarCursoRequestDto request
    ) {
        Curso curso = cursoService.atualizar(id, request.getNome(), request.getDescricao());

        return ResponseEntity.ok(CursoResponseDto.fromEntity(curso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        cursoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}