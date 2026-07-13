import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CriarMatriculaRequest, Matricula } from '../models/matricula.model';

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {

  private readonly apiUrl = '/api/matriculas';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(this.apiUrl);
  }

  criar(request: CriarMatriculaRequest): Observable<Matricula> {
    return this.http.post<Matricula>(this.apiUrl, request);
  }

  confirmar(id: number): Observable<Matricula> {
    return this.http.patch<Matricula>(`${this.apiUrl}/${id}/confirmar`, {});
  }

  cancelar(id: number): Observable<Matricula> {
    return this.http.patch<Matricula>(`${this.apiUrl}/${id}/cancelar`, {});
  }

  listarPorAluno(alunoId: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.apiUrl}/aluno/${alunoId}`);
  }

  listarPorTurma(turmaId: number): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.apiUrl}/turma/${turmaId}`);
  }
}