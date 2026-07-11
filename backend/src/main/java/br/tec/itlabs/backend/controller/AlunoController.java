package br.tec.itlabs.backend.controller;

import br.tec.itlabs.backend.dto.aluno.AlunoResponseDto;
import br.tec.itlabs.backend.dto.aluno.AtualizarAlunoRequestDto;
import br.tec.itlabs.backend.dto.aluno.CriarAlunoRequestDto;
import br.tec.itlabs.backend.entity.Aluno;
import br.tec.itlabs.backend.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDto> criar(@RequestBody @Valid CriarAlunoRequestDto request) {
        Aluno aluno = alunoService.criar(request.getNome(), request.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AlunoResponseDto.fromEntity(aluno));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDto>> listar() {
        List<AlunoResponseDto> alunos = alunoService.listar()
                .stream()
                .map(aluno -> AlunoResponseDto.fromEntity(aluno))
                .toList();
        return ResponseEntity.ok(alunos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDto> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid AtualizarAlunoRequestDto request) {
        Aluno aluno = alunoService.atualizar(id, request.getNome(), request.getEmail());

        return ResponseEntity.ok(AlunoResponseDto.fromEntity(aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alunoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
