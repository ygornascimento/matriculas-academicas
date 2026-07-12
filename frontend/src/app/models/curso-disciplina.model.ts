export interface CursoDisciplina {
  id: number;
  cursoId: number;
  disciplinaId: number;
  criadoEm: string;
}

export interface CriarCursoDisciplinaRequest {
  cursoId: number | null;
  disciplinaId: number | null;
}