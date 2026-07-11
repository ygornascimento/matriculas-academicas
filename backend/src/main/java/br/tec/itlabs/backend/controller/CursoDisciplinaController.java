package br.tec.itlabs.backend.controller;

import br.tec.itlabs.backend.dto.cursodisciplina.CriarCursoDisciplinaRequestDto;
import br.tec.itlabs.backend.dto.cursodisciplina.CursoDisciplinaResponseDto;
import br.tec.itlabs.backend.entity.CursoDisciplina;
import br.tec.itlabs.backend.service.CursoDisciplinaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/curso-disciplinas")
public class CursoDisciplinaController {

    private final CursoDisciplinaService cursoDisciplinaService;

    public CursoDisciplinaController(CursoDisciplinaService cursoDisciplinaService) {
        this.cursoDisciplinaService = cursoDisciplinaService;
    }

    @PostMapping
    public ResponseEntity<CursoDisciplinaResponseDto> associar(
            @RequestBody @Valid CriarCursoDisciplinaRequestDto request
    ) {
        CursoDisciplina cursoDisciplina = cursoDisciplinaService.associar(
                request.getCursoId(),
                request.getDisciplinaId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CursoDisciplinaResponseDto.fromEntity(cursoDisciplina));
    }

    @GetMapping
    public ResponseEntity<List<CursoDisciplinaResponseDto>> listar() {
        List<CursoDisciplinaResponseDto> cursoDisciplinas = cursoDisciplinaService.listar()
                .stream()
                .map(CursoDisciplinaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(cursoDisciplinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDisciplinaResponseDto> buscarPorId(@PathVariable Long id) {
        CursoDisciplina cursoDisciplina = cursoDisciplinaService.buscarPorId(id);

        return ResponseEntity.ok(CursoDisciplinaResponseDto.fromEntity(cursoDisciplina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cursoDisciplinaService.remover(id);

        return ResponseEntity.noContent().build();
    }
}