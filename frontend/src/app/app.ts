import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Aluno } from './models/aluno.model';
import { Curso } from './models/curso.model';
import { Disciplina } from './models/disciplina.model';
import { CursoDisciplina } from './models/curso-disciplina.model';

import { AlunoService } from './services/aluno.service';
import { CursoService } from './services/curso.service';
import { DisciplinaService } from './services/disciplina.service';
import { CursoDisciplinaService } from './services/curso-disciplina.service';

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

  mensagem = '';
  erro = '';

  constructor(
    private readonly alunoService: AlunoService,
    private readonly cursoService: CursoService,
    private readonly disciplinaService: DisciplinaService,
    private readonly cursoDisciplinaService: CursoDisciplinaService
  ) {}

  ngOnInit(): void {
    this.carregarAlunos();
    this.carregarCursos();
    this.carregarDisciplinas();
    this.carregarCursoDisciplinas();
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

buscarNomeCurso(cursoId: number): string {
  return this.cursos.find(curso => curso.id === cursoId)?.nome ?? `Curso ${cursoId}`;
}

buscarNomeDisciplina(disciplinaId: number): string {
  return this.disciplinas.find(disciplina => disciplina.id === disciplinaId)?.nome ?? `Disciplina ${disciplinaId}`;
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