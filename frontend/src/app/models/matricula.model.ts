export interface Matricula {
  id: number;
  alunoId: number;
  turmaId: number;
  status: string;
  criadoEm: string;
  confirmadaEm: string | null;
  canceladaEm: string | null;
}

export interface CriarMatriculaRequest {
  alunoId: number | null;
  turmaId: number | null;
}