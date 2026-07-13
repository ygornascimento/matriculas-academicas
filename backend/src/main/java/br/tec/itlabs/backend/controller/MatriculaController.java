package br.tec.itlabs.backend.controller;

import br.tec.itlabs.backend.dto.matricula.CriarMatriculaRequestDto;
import br.tec.itlabs.backend.dto.matricula.MatriculaResponseDto;
import br.tec.itlabs.backend.entity.Matricula;
import br.tec.itlabs.backend.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @PostMapping
    public ResponseEntity<MatriculaResponseDto> criar(@RequestBody @Valid CriarMatriculaRequestDto request) {
        Matricula matricula = matriculaService.criar(
                request.getAlunoId(),
                request.getTurmaId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MatriculaResponseDto.fromEntity(matricula));
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDto>> listar() {
        List<MatriculaResponseDto> matriculas = matriculaService.listar()
                .stream()
                .map(MatriculaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(matriculas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDto> buscarPorId(@PathVariable Long id) {
        Matricula matricula = matriculaService.buscarPorId(id);

        return ResponseEntity.ok(MatriculaResponseDto.fromEntity(matricula));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<MatriculaResponseDto>> listarPorAluno(@PathVariable Long alunoId) {
        List<MatriculaResponseDto> matriculas = matriculaService.listarPorAluno(alunoId)
                .stream()
                .map(MatriculaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(matriculas);
    }

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<MatriculaResponseDto>> listarPorTurma(@PathVariable Long turmaId) {
        List<MatriculaResponseDto> matriculas = matriculaService.listarPorTurma(turmaId)
                .stream()
                .map(MatriculaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(matriculas);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<MatriculaResponseDto> confirmar(@PathVariable Long id) {
        Matricula matricula = matriculaService.confirmar(id);

        return ResponseEntity.ok(MatriculaResponseDto.fromEntity(matricula));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<MatriculaResponseDto> cancelar(@PathVariable Long id) {
        Matricula matricula = matriculaService.cancelar(id);

        return ResponseEntity.ok(MatriculaResponseDto.fromEntity(matricula));
    }
}