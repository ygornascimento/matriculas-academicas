export interface Curso {
  id: number;
  nome: string;
  descricao: string;
  criadoEm: string;
  atualizadoEm: string;
}

export interface CriarCursoRequest {
  nome: string;
  descricao: string;
}