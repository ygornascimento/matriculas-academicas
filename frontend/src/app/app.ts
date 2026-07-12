import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Aluno } from './models/aluno.model';
import { AlunoService } from './services/aluno.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

  alunos: Aluno[] = [];

  novoAluno = {
    nome: '',
    email: ''
  };

  mensagem = '';
  erro = '';

  constructor(private readonly alunoService: AlunoService) {}

  ngOnInit(): void {
    this.carregarAlunos();
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

  criarAluno(): void {
    this.mensagem = '';
    this.erro = '';

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

  private extrairMensagemErro(error: any, mensagemPadrao: string): string {
    if (error.error?.campos?.length > 0) {
      return error.error.campos[0].mensagem;
    }

    return error.error?.mensagem ?? mensagemPadrao;
  }
}