export interface Aluno {
  id: number;
  nome: string;
  email: string;
  criadoEm: string;
  atualizadoEm: string;
}

export interface CriarAlunoRequest {
  nome: string;
  email: string;
}