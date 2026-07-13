export interface Turma {
  id: number;
  cursoDisciplinaId: number;
  codigo: string;
  periodo: string;
  limiteVagas: number;
  vagasOcupadas: number;
  status: string;
  criadoEm: string;
  atualizadoEm: string;
}

export interface CriarTurmaRequest {
  cursoDisciplinaId: number | null;
  codigo: string;
  periodo: string;
  limiteVagas: number | null;
}