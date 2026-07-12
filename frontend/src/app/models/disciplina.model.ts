export interface Disciplina {
  id: number;
  nome: string;
  cargaHoraria: number;
  criadoEm: string;
  atualizadoEm: string;
}

export interface CriarDisciplinaRequest {
  nome: string;
  cargaHoraria: number | null;
}