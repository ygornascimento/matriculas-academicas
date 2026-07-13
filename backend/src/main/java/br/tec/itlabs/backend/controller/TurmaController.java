package br.tec.itlabs.backend.controller;

import br.tec.itlabs.backend.dto.turma.AtualizarTurmaRequestDto;
import br.tec.itlabs.backend.dto.turma.CriarTurmaRequestDto;
import br.tec.itlabs.backend.dto.turma.TurmaResponseDto;
import br.tec.itlabs.backend.entity.Turma;
import br.tec.itlabs.backend.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    public ResponseEntity<TurmaResponseDto> criar(@RequestBody @Valid CriarTurmaRequestDto request) {
        Turma turma = turmaService.criar(
                request.getCursoDisciplinaId(),
                request.getCodigo(),
                request.getPeriodo(),
                request.getLimiteVagas()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TurmaResponseDto.fromEntity(turma));
    }

    @GetMapping
    public ResponseEntity<List<TurmaResponseDto>> listar() {
        List<TurmaResponseDto> turmas = turmaService.listar()
                .stream()
                .map(TurmaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDto> buscarPorId(@PathVariable Long id) {
        Turma turma = turmaService.buscarPorId(id);

        return ResponseEntity.ok(TurmaResponseDto.fromEntity(turma));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarTurmaRequestDto request
    ) {
        Turma turma = turmaService.atualizar(
                id,
                request.getCursoDisciplinaId(),
                request.getCodigo(),
                request.getPeriodo(),
                request.getLimiteVagas()
        );

        return ResponseEntity.ok(TurmaResponseDto.fromEntity(turma));
    }

    @PatchMapping("/{id}/abrir")
    public ResponseEntity<TurmaResponseDto> abrir(@PathVariable Long id) {
        Turma turma = turmaService.abrir(id);

        return ResponseEntity.ok(TurmaResponseDto.fromEntity(turma));
    }

    @PatchMapping("/{id}/fechar")
    public ResponseEntity<TurmaResponseDto> fechar(@PathVariable Long id) {
        Turma turma = turmaService.fechar(id);

        return ResponseEntity.ok(TurmaResponseDto.fromEntity(turma));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        turmaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}