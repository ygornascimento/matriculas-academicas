package br.tec.itlabs.backend.service;

import br.tec.itlabs.backend.entity.Aluno;
import br.tec.itlabs.backend.entity.Curso;
import br.tec.itlabs.backend.entity.CursoDisciplina;
import br.tec.itlabs.backend.entity.Disciplina;
import br.tec.itlabs.backend.entity.Matricula;
import br.tec.itlabs.backend.entity.StatusMatricula;
import br.tec.itlabs.backend.entity.StatusTurma;
import br.tec.itlabs.backend.entity.Turma;
import br.tec.itlabs.backend.exception.RegraDeNegocioException;
import br.tec.itlabs.backend.repository.AlunoRepository;
import br.tec.itlabs.backend.repository.MatriculaRepository;
import br.tec.itlabs.backend.repository.TurmaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private MatriculaService matriculaService;

    private Aluno aluno;
    private Turma turma;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("Aluno Teste", "aluno.teste@email.com");

        Curso curso = new Curso("Curso Teste", "Curso usado nos testes.");
        Disciplina disciplina = new Disciplina("Disciplina Teste", 80);
        CursoDisciplina cursoDisciplina = new CursoDisciplina(curso, disciplina);

        turma = new Turma(
                cursoDisciplina,
                "TURMA-TESTE-01",
                "2026.2",
                1
        );
    }

    @Test
    void deveCriarMatriculaPendenteQuandoTurmaEstaAbertaEComVaga() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
        when(matriculaRepository.existsByAlunoIdAndTurmaId(1L, 1L)).thenReturn(false);
        when(matriculaRepository.save(any(Matricula.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Matricula matricula = matriculaService.criar(1L, 1L);

        assertEquals(StatusMatricula.PENDENTE, matricula.getStatus());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(turma, matricula.getTurma());
        assertEquals(0, turma.getVagasOcupadas());
        assertEquals(StatusTurma.ABERTA, turma.getStatus());
    }

    @Test
    void deveBloquearMatriculaDuplicadaParaMesmoAlunoETurma() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
        when(matriculaRepository.existsByAlunoIdAndTurmaId(1L, 1L)).thenReturn(true);

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> matriculaService.criar(1L, 1L)
        );

        assertEquals("Aluno já possui matrícula nesta turma.", exception.getMessage());
    }

    @Test
    void deveConfirmarMatriculaConsumindoVagaEDeixandoTurmaCompleta() {
        Matricula matricula = new Matricula(aluno, turma);

        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(turmaRepository.save(any(Turma.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(matriculaRepository.save(any(Matricula.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Matricula matriculaConfirmada = matriculaService.confirmar(1L);

        assertEquals(StatusMatricula.CONFIRMADA, matriculaConfirmada.getStatus());
        assertEquals(1, turma.getVagasOcupadas());
        assertEquals(StatusTurma.COMPLETA, turma.getStatus());
    }

    @Test
    void deveCancelarMatriculaConfirmadaLiberandoVagaEReabrindoTurma() {
        Matricula matricula = new Matricula(aluno, turma);

        turma.consumirVaga();
        matricula.confirmar();

        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(turmaRepository.save(any(Turma.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(matriculaRepository.save(any(Matricula.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Matricula matriculaCancelada = matriculaService.cancelar(1L);

        assertEquals(StatusMatricula.CANCELADA, matriculaCancelada.getStatus());
        assertEquals(0, turma.getVagasOcupadas());
        assertEquals(StatusTurma.ABERTA, turma.getStatus());
    }

    private void matriculaServiceConfirmarDiretamente(Matricula matricula, Turma turma) {
        turma.consumirVaga();
        matricula.confirmar();
    }
}