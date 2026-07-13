import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Aluno } from './models/aluno.model';
import { Curso } from './models/curso.model';
import { Disciplina } from './models/disciplina.model';
import { CursoDisciplina } from './models/curso-disciplina.model';
import { Turma } from './models/turma.model';

import { AlunoService } from './services/aluno.service';
import { CursoService } from './services/curso.service';
import { DisciplinaService } from './services/disciplina.service';
import { CursoDisciplinaService } from './services/curso-disciplina.service';
import { TurmaService } from './services/turma.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

  alunos: Aluno[] = [];
  cursos: Curso[] = [];
  disciplinas: Disciplina[] = [];
  cursoDisciplinas: CursoDisciplina[] = [];
  turmas: Turma[] = [];

  novoAluno = {
    nome: '',
    email: ''
  };

  novoCurso = {
    nome: '',
    descricao: ''
  };

  novaDisciplina = {
    nome: '',
    cargaHoraria: null as number | null
  };

  novaAssociacao = {
    cursoId: null as number | null,
    disciplinaId: null as number | null
  };

  novaTurma = {
    cursoDisciplinaId: null as number | null,
    codigo: '',
    periodo: '',
    limiteVagas: null as number | null
  };

  mensagem = '';
  erro = '';

  abaAtual: 'cadastros' | 'estrutura' | 'matriculas' | 'consultas' = 'cadastros';

  constructor(
    private readonly alunoService: AlunoService,
    private readonly cursoService: CursoService,
    private readonly disciplinaService: DisciplinaService,
    private readonly cursoDisciplinaService: CursoDisciplinaService,
    private readonly turmaService: TurmaService
  ) {}

  ngOnInit(): void {
    this.carregarAlunos();
    this.carregarCursos();
    this.carregarDisciplinas();
    this.carregarCursoDisciplinas();
    this.carregarTurmas();
  }

  carregarAlunos(): void {
    this.alunoService.listar().subscribe({
      next: alunos => {
        this.alunos = alunos;
      },
      error: () => {
        this.erro = 'Erro ao carregar alunos.';
      }
    });
  }

  carregarCursos(): void {
    this.cursoService.listar().subscribe({
      next: cursos => {
        this.cursos = cursos;
      },
      error: () => {
        this.erro = 'Erro ao carregar cursos.';
      }
    });
  }

  carregarDisciplinas(): void {
    this.disciplinaService.listar().subscribe({
      next: disciplinas => {
        this.disciplinas = disciplinas;
      },
      error: () => {
        this.erro = 'Erro ao carregar disciplinas.';
      }
    });
  }

  carregarCursoDisciplinas(): void {
    this.cursoDisciplinaService.listar().subscribe({
      next: cursoDisciplinas => {
        this.cursoDisciplinas = cursoDisciplinas;
      },
      error: () => {
        this.erro = 'Erro ao carregar associações entre cursos e disciplinas.';
      }
    });
  }

  carregarTurmas(): void {
    this.turmaService.listar().subscribe({
      next: turmas => {
        this.turmas = turmas;
      },
      error: () => {
        this.erro = 'Erro ao carregar turmas.';
      }
    });
  }

  criarAluno(): void {
    this.limparMensagens();

    this.alunoService.criar(this.novoAluno).subscribe({
      next: aluno => {
        this.mensagem = `Aluno ${aluno.nome} cadastrado com sucesso.`;

        this.novoAluno = {
          nome: '',
          email: ''
        };

        this.carregarAlunos();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(error, 'Erro ao cadastrar aluno.');
      }
    });
  }

  criarCurso(): void {
    this.limparMensagens();

    this.cursoService.criar(this.novoCurso).subscribe({
      next: curso => {
        this.mensagem = `Curso ${curso.nome} cadastrado com sucesso.`;

        this.novoCurso = {
          nome: '',
          descricao: ''
        };

        this.carregarCursos();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(error, 'Erro ao cadastrar curso.');
      }
    });
  }

  criarDisciplina(): void {
    this.limparMensagens();

    this.disciplinaService.criar(this.novaDisciplina).subscribe({
      next: disciplina => {
        this.mensagem = `Disciplina ${disciplina.nome} cadastrada com sucesso.`;

        this.novaDisciplina = {
          nome: '',
          cargaHoraria: null
        };

        this.carregarDisciplinas();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(error, 'Erro ao cadastrar disciplina.');
      }
    });
  }

  associarCursoDisciplina(): void {
    this.limparMensagens();

    this.cursoDisciplinaService.associar(this.novaAssociacao).subscribe({
      next: () => {
        this.mensagem = 'Disciplina associada ao curso com sucesso.';

        this.novaAssociacao = {
          cursoId: null,
          disciplinaId: null
        };

        this.carregarCursoDisciplinas();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(
          error,
          'Erro ao associar disciplina ao curso.'
        );
      }
    });
  }

  criarTurma(): void {
    this.limparMensagens();

    this.turmaService.criar(this.novaTurma).subscribe({
      next: turma => {
        this.mensagem = `Turma ${turma.codigo} cadastrada com sucesso.`;

        this.novaTurma = {
          cursoDisciplinaId: null,
          codigo: '',
          periodo: '',
          limiteVagas: null
        };

        this.carregarTurmas();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(error, 'Erro ao cadastrar turma.');
      }
    });
  }

  abrirTurma(turmaId: number): void {
    this.limparMensagens();

    this.turmaService.abrir(turmaId).subscribe({
      next: turma => {
        this.mensagem = `Turma ${turma.codigo} aberta com sucesso.`;
        this.carregarTurmas();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(error, 'Erro ao abrir turma.');
      }
    });
  }

  fecharTurma(turmaId: number): void {
    this.limparMensagens();

    this.turmaService.fechar(turmaId).subscribe({
      next: turma => {
        this.mensagem = `Turma ${turma.codigo} fechada com sucesso.`;
        this.carregarTurmas();
      },
      error: error => {
        this.erro = this.extrairMensagemErro(error, 'Erro ao fechar turma.');
      }
    });
  }

  buscarDescricaoCursoDisciplina(cursoDisciplinaId: number): string {
    const associacao = this.cursoDisciplinas.find(
      cursoDisciplina => cursoDisciplina.id === cursoDisciplinaId
    );

    if (!associacao) {
      return `Associação ${cursoDisciplinaId}`;
    }

    const curso = this.buscarNomeCurso(associacao.cursoId);
    const disciplina = this.buscarNomeDisciplina(associacao.disciplinaId);

    return `${curso} / ${disciplina}`;
  }

  buscarNomeCurso(cursoId: number): string {
    return this.cursos.find(curso => curso.id === cursoId)?.nome ?? `Curso ${cursoId}`;
  }

  buscarNomeDisciplina(disciplinaId: number): string {
    return this.disciplinas.find(disciplina => disciplina.id === disciplinaId)?.nome ?? `Disciplina ${disciplinaId}`;
  }

  alterarAba(aba: 'cadastros' | 'estrutura' | 'matriculas' | 'consultas'): void {
    this.abaAtual = aba;

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  private limparMensagens(): void {
    this.mensagem = '';
    this.erro = '';
  }

  private extrairMensagemErro(error: any, mensagemPadrao: string): string {
    if (error.error?.campos?.length > 0) {
      return error.error.campos[0].mensagem;
    }

    return error.error?.mensagem ?? mensagemPadrao;
  }
}